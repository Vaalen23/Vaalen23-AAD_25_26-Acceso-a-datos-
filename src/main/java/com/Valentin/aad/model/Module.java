package com.Valentin.aad.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module {
    private Integer id;
    private String codigo;
    private String nombre;
    private Integer horas;
}