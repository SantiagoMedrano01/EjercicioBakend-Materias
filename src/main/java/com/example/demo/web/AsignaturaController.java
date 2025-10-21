package com.example.demo.web;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Asignatura;
import com.example.demo.domain.Comision;
import com.example.demo.dto.AsignaturaResumen;
import com.example.demo.repo.MateriasRepo;

import jakarta.validation.Valid;


@RestController
public class AsignaturaController {
    private final MateriasRepo repo;

    public AsignaturaController(MateriasRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/api/asignaturas")
    public ResponseEntity<List<Asignatura>> getAsignaturas(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) Integer cuat,
        @RequestParam(required = false) Character turno) {

    List<Asignatura> asignaturas = repo.findByFilters(q, cuat, turno);

    if (asignaturas.isEmpty()) {
        return ResponseEntity.noContent().build(); // 204 No Content (mejor que 404)
    }

    return ResponseEntity.ok(asignaturas); // 200 OK con la lista
}

    @GetMapping("/api/asignaturas/resumen")
    public ResponseEntity<List<AsignaturaResumen>> getAsignaturasResumen() {
        List<AsignaturaResumen> asignaturas = repo.findResumen(); 
        if (asignaturas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(asignaturas); // devulve una lista json de los objetos y devulve el estado 200

    }

    @GetMapping ("/api/asignaturas/{codigo}")
    public ResponseEntity<Asignatura> getAsignaturaPorCodigo(@PathVariable String codigo){
        Optional<Asignatura> asignatura =repo.findByCodigo(codigo);
        return asignatura.map(ResponseEntity::ok) // devulve el json del objeto y devulve el estado 200
                .orElseGet(() -> ResponseEntity.notFound().build()); //  el error  devulve el estado 404
    }

    @PostMapping ("/api/asignaturas")
    public ResponseEntity<Asignatura> crearAsignatura(@Valid @RequestBody Asignatura nuevaAsignatura){
        Asignatura asignaturaCreada = repo.save(nuevaAsignatura);
        return ResponseEntity.ok(asignaturaCreada); // devulve el json del objeto y devulve el estado 200
    }

    @PutMapping("/api/asignaturas/{codigo}")
    public ResponseEntity<Asignatura> actualizarAsignatura(
        @PathVariable String codigo,
        @Valid @RequestBody Asignatura datosNuevos) {

    return repo.findByCodigo(codigo)
            .map(asig -> {
                asig.setNombre(datosNuevos.getNombre());
                asig.setCreditos(datosNuevos.getCreditos());
                asig.setCuatrimestre(datosNuevos.getCuatrimestre());
                repo.save(asig);
                return ResponseEntity.ok(asig);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
}


    @DeleteMapping ("/api/asignaturas/{codigo}")
    public ResponseEntity<Void> eliminarAsignatura(@PathVariable String codigo){
        Optional<Asignatura> asignaturaExistente = repo.findByCodigo(codigo);
        if (asignaturaExistente.isPresent()) {
            repo.delete(codigo);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); //  el error  devulve el estado 404
        }
    }

    @GetMapping("/api/asignaturas/{codigo}/comisiones")
    public ResponseEntity<List<Comision>> getComisionesPorCodigo(@Valid @PathVariable String codigo){
        List<Comision> comisiones = repo.findComisionesByCodigo(codigo);
        if (comisiones.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(comisiones); // devulve una lista json de los objetos y devulve el estado 200
    }

    @PostMapping("/api/asignaturas/{codigo}/comisiones")
    public ResponseEntity<Comision> crearComision(
        @PathVariable String codigo,
        @RequestBody Comision nuevaComision) {

    Optional<Asignatura> asignaturaOpt = repo.findByCodigo(codigo);
    if (asignaturaOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Asignatura asignatura = asignaturaOpt.get();

    // Generar ID para la nueva comisión (si no lo manejás automáticamente)
    Long nuevoId = asignatura.getComisiones().stream()
            .map(Comision::getId)
            .filter(Objects::nonNull)
            .max(Long::compareTo)
            .orElse(0L) + 1;

    nuevaComision.setId(nuevoId);
    nuevaComision.setCodigoAsignatura(codigo);

    // Agregar a la lista
    asignatura.getComisiones().add(nuevaComision);

    // Guardar la asignatura actualizada
    repo.save(asignatura);

    return ResponseEntity.status(HttpStatus.CREATED).body(nuevaComision);
}

    @GetMapping("/api/comisiones/{id}")
    public ResponseEntity<Comision> getComisionPorId(@PathVariable Long id){
        Optional<Comision> comision =repo.findComisionById(id);
        return comision.map(ResponseEntity::ok) // devulve el json del objeto y devulve el estado 200
                .orElseGet(() -> ResponseEntity.notFound().build()); //  el error  devulve el estado 404
    }

}