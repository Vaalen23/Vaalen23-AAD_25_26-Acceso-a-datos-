package com.example.aad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

// Clase que representa la matricula de un alumno en un modulo
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Matricula {

    // La tabla usa clave primaria compuesta (id_alumno, id_modulo)
    private Integer idAlumno;
    private Integer idModulo;
    private LocalDate fecha;
}
