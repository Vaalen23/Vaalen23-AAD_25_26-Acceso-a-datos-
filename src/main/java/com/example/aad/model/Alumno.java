package com.example.aad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Clase que representa un alumno en el sistema academico
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Alumno {

    private Integer id;
    private String nif;
    private String nombre;
    private String email;
}
