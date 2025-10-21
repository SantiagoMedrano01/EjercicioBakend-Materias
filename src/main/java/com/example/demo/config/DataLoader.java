package com.example.demo.config;



import com.example.demo.domain.Asignatura;
import com.example.demo.domain.Comision;
import com.example.demo.domain.Slot;
import com.example.demo.repo.MateriasRepo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final MateriasRepo repo;

    public DataLoader(MateriasRepo repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        // Slots "plantilla" por turno
        List<Slot> slotsM = List.of(
                new Slot("LU", LocalTime.of(8, 0), LocalTime.of(10, 0), "Aula 101"),
                new Slot("MI", LocalTime.of(8, 0), LocalTime.of(10, 0), "Aula 101"),
                new Slot("VI", LocalTime.of(8, 0), LocalTime.of(9, 30), "Aula 102")
        );
        List<Slot> slotsT = List.of(
                new Slot("MA", LocalTime.of(14, 0), LocalTime.of(16, 0), "Aula 201"),
                new Slot("JU", LocalTime.of(14, 0), LocalTime.of(16, 0), "Aula 202")
        );
        List<Slot> slotsN = List.of(
                new Slot("LU", LocalTime.of(19, 0), LocalTime.of(21, 0), "Aula 301"),
                new Slot("JU", LocalTime.of(19, 0), LocalTime.of(21, 0), "Aula 301")
        );

        // === Ejemplo de 3 materias con comisiones ===
        Asignatura analisis = new Asignatura("ISI-1AMI", "Análisis Matemático I", 6, 1, List.of(
                new Comision(1L, "ISI-1AMI", 'M', slotsM),
                new Comision(2L, "ISI-1AMI", 'N', slotsN)
        ));

        Asignatura prog1 = new Asignatura("ISI-1P1", "Programación I", 6, 1, List.of(
                new Comision(3L, "ISI-1P1", 'M', slotsM),
                new Comision(4L, "ISI-1P1", 'T', slotsT)
        ));

        Asignatura algebra = new Asignatura("ISI-1ALG", "Álgebra y Geometría", 6, 1, List.of(
                new Comision(5L, "ISI-1ALG", 'T', slotsT),
                new Comision(6L, "ISI-1ALG", 'N', slotsN)
        ));

        // Guardar todas en el repositorio
        repo.saveAll(List.of(analisis, prog1, algebra));

        System.out.println("✅ Dataset de asignaturas cargado en memoria: " + repo.findAll().size());
    }
}

