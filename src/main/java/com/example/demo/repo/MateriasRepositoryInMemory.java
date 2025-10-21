package com.example.demo.repo;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.*;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.Asignatura;
import com.example.demo.domain.Comision;
import com.example.demo.dto.AsignaturaResumen;



@Repository
public class MateriasRepositoryInMemory implements MateriasRepo {

    private final Map<String, Asignatura> materias = new ConcurrentHashMap<>();

    @Override
    public List<Asignatura> findAll() {
        return new ArrayList<>(materias.values());
    }

    @Override
    public List<Asignatura> findByFilters(String q, Integer cuatrimestre, Character turno) {
        return materias.values().stream()
                .filter(a -> q == null || q.isBlank()
                        || containsIgnoreCase(a.getCodigo(), q)
                        || containsIgnoreCase(a.getNombre(), q))
                .filter(a -> cuatrimestre == null || a.getCuatrimestre() == cuatrimestre)
                .filter(a -> turno == null || a.getComisiones().stream()
                        .anyMatch(c -> c.getTurno() != null
                                && Character.toUpperCase(c.getTurno()) == Character.toUpperCase(turno)))
                .sorted(Comparator.comparing(Asignatura::getCodigo))
                .collect(Collectors.toList());
    }

    @Override
    public List<AsignaturaResumen> findResumen() {
        return materias.values().stream()
                .sorted(Comparator.comparing(Asignatura::getCodigo))
                .map(a -> new AsignaturaResumen(
                        a.getCodigo(), a.getNombre(), a.getCreditos(), a.getCuatrimestre()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Asignatura> findByCodigo(String codigo) {
        return Optional.ofNullable(materias.get(codigo));
    }

    @Override
    public Asignatura save(Asignatura asignatura) {
        Objects.requireNonNull(asignatura, "asignatura no puede ser null");
        Objects.requireNonNull(asignatura.getCodigo(), "codigo no puede ser null");
        materias.put(asignatura.getCodigo(), asignatura);
        return asignatura;
    }

    @Override
    public boolean delete(String codigo) {
        return materias.remove(codigo) != null;
    }

    @Override
    public List<Comision> findComisionesByCodigo(String codigo) {
        return Optional.ofNullable(materias.get(codigo))
                .map(Asignatura::getComisiones)
                .map(ArrayList::new) // copia defensiva
                .orElseGet(ArrayList::new);
    }

    @Override
    public Optional<Comision> findComisionById(Long id) {
        if (id == null) return Optional.empty();
        return materias.values().stream()
                .flatMap(a -> a.getComisiones().stream())
                .filter(c -> id.equals(c.getId()))
                .findFirst();
    }

    @Override
    public void saveAll(List<Asignatura> asignaturas) {
        if (asignaturas == null) return;
        for (Asignatura a : asignaturas) save(a);
    }

    private static boolean containsIgnoreCase(String source, String needle) {
        return source != null && needle != null
                && source.toLowerCase().contains(needle.toLowerCase());
    }
}

