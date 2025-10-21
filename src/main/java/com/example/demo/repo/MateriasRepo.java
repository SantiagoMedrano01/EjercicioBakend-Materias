package com.example.demo.repo;

import com.example.demo.domain.Asignatura;
import com.example.demo.domain.Comision;
import com.example.demo.dto.AsignaturaResumen;

import java.util.List;
import java.util.Optional;

public interface MateriasRepo {

    List<Asignatura> findAll();

    List<Asignatura> findByFilters(String q, Integer cuatrimestre, Character turno);

    // Devuelve un resumen sin comisiones (para /resumen)
    List<AsignaturaResumen> findResumen();

    Optional<Asignatura> findByCodigo(String codigo);

    Asignatura save(Asignatura asignatura);

    boolean delete(String codigo);

    List<Comision> findComisionesByCodigo(String codigo);

    Optional<Comision> findComisionById(Long id);

    // Para precargar dataset (DataLoader)
    void saveAll(List<Asignatura> asignaturas);

    // ===== DTO “proyección” para el resumen =====
    
}
