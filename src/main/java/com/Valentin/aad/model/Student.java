package com.Valentin.aad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Student {
    private Integer id;
    private String nif;
    private String nombre;
    private String email;
    private String curso;
    private List<Module> modulos;
}
