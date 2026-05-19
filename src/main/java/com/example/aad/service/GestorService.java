package com.example.aad.service;

import java.nio.charset.Charset;
import java.time.LocalDate;

public interface GestorService {

    void anadirEvento(String mensaje, Charset charset);

    void mostrarTodos(Charset charset);

    void filtrarPorFecha(LocalDate fecha, Charset charset);

    void cargarEventosEjemplo(Charset charset);
}
