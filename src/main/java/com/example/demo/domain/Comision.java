package com.example.demo.domain;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comision {
    private Long id;
    private String codigoAsignatura;

    @NotNull
    @Pattern(regexp = "[MTN]")
    private Character turno;
    
    private List<Slot> slots;
}
