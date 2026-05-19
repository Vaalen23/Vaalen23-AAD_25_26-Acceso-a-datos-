package com.example.aad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Clase que representa un modulo del ciclo formativo
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Modulo {

    private Integer id;
    private String codigo;
    private String nombre;
    private Integer horas;
}
