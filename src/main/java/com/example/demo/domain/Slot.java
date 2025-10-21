package com.example.demo.domain;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    private String dia;
    @NotNull
    @DateTimeFormat(pattern = "HH:mm"   )
    private LocalTime desde;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm"   )
    private LocalTime hasta;

    private String aula;
}
