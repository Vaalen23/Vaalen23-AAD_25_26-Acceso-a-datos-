package com.example.aad.util;

import java.time.format.DateTimeFormatter;

public final class Constantes {

    public static final String FICHERO_LOG = "app.log";
    public static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Constantes() {
    }
}
