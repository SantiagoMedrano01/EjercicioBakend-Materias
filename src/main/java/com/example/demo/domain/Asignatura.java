package com.example.demo.domain;


import lombok.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asignatura {
    @NotBlank
    private String codigo;
    @NotBlank
    private String nombre;
    
    private int creditos;

    @Min(1)
    @Max(2)
    private int cuatrimestre;
    
    private List<Comision> comisiones = new ArrayList<>();
}

