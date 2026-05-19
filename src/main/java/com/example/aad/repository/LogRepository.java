package com.example.aad.repository;

import com.example.aad.model.EventoLog;
import com.example.aad.util.Constantes;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LogRepository {

    public void crearFicheroSiNoExiste() throws IOException {
        File fichero = new File(Constantes.FICHERO_LOG);
        if (!fichero.exists()) {
            fichero.createNewFile();
        }
    }

    public void guardar(EventoLog eventoLog, Charset charset) throws IOException {
        crearFicheroSiNoExiste();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Constantes.FICHERO_LOG, true), charset))) {
            writer.write(formatearEvento(eventoLog));
            writer.newLine();
        }
    }

    public List<String> leerTodos(Charset charset) throws IOException {
        crearFicheroSiNoExiste();
        List<String> lineas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(Constantes.FICHERO_LOG), charset))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.isBlank()) {
                    lineas.add(linea);
                }
            }
        }
        return lineas;
    }

    public List<String> filtrarPorFecha(LocalDate fecha, Charset charset) throws IOException {
        List<String> lineas = leerTodos(charset);
        String prefijo = "[" + fecha.format(Constantes.FORMATO_FECHA);
        return lineas.stream()
                .filter(linea -> linea.startsWith(prefijo))
                .toList();
    }

    private String formatearEvento(EventoLog eventoLog) {
        return "[" + eventoLog.getFechaHora().format(Constantes.FORMATO_FECHA_HORA) + "] " + eventoLog.getMensaje();
    }
}
