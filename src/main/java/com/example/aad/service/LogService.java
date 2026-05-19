package com.example.aad.service;

import com.example.aad.model.EventoLog;
import com.example.aad.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService implements GestorService {

    private final LogRepository logRepository;

    @Override
    public void anadirEvento(String mensaje, Charset charset) {
        try {
            EventoLog eventoLog = new EventoLog(LocalDateTime.now(), mensaje);
            logRepository.guardar(eventoLog, charset);
            System.out.println("Evento guardado correctamente en app.log");
        } catch (IOException e) {
            System.out.println("Error al guardar el evento: " + e.getMessage());
        }
    }

    @Override
    public void mostrarTodos(Charset charset) {
        try {
            List<String> eventos = logRepository.leerTodos(charset);
            if (eventos.isEmpty()) {
                System.out.println("No hay eventos registrados.");
                return;
            }
            eventos.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }

    @Override
    public void filtrarPorFecha(LocalDate fecha, Charset charset) {
        try {
            List<String> eventos = logRepository.filtrarPorFecha(fecha, charset);
            if (eventos.isEmpty()) {
                System.out.println("No hay eventos registrados para la fecha indicada.");
                return;
            }
            eventos.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error al filtrar eventos: " + e.getMessage());
        }
    }

    @Override
    public void cargarEventosEjemplo(Charset charset) {
        String[] mensajes = {
                "Usuario Valentin inicio sesion",
                "Usuario Paco consulto los registros",
                "Usuario Vito modifico la configuracion",
                "Usuario Ruben filtro eventos por fecha",
                "Usuario Carlos cerro sesion",
                "Usuario Kiko genero una copia del log"
        };
        for (String mensaje : mensajes) {
            anadirEvento(mensaje, charset);
        }
    }
}
