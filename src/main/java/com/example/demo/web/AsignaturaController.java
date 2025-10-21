package com.example.demo.web;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Asignatura;
import com.example.demo.dto.AsignaturaResumen;
import com.example.demo.repo.MateriasRepo;


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
        return ResponseEntity.ok(asignaturas); 

    }

    @GetMapping ("/api/asignaturas/{codigo}")
    public ResponseEntity<Asignatura> getAsignaturaPorCodigo(@PathVariable String codigo){
        Optional<Asignatura> asignatura =repo.findByCodigo(codigo);
        return asignatura.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
