package com.example.aad.repository;

import com.example.aad.model.Alumno;
import com.example.aad.util.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CsvAlumnoRepository {

    private static final Logger log = LoggerFactory.getLogger(CsvAlumnoRepository.class);

    public List<Alumno> leerTodos() throws Exception {
        List<Alumno> alumnos = new ArrayList<>();
        ClassPathResource recurso = new ClassPathResource(Constantes.FICHERO_CSV);

        if (!recurso.exists()) {
            throw new Exception("No se ha encontrado el fichero " + Constantes.FICHERO_CSV);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(recurso.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            int numeroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;

                if (linea.isBlank()) {
                    continue;
                }

                if (numeroLinea == 1) {
                    validarCabecera(linea);
                    continue;
                }

                alumnos.add(convertirLineaEnAlumno(linea, numeroLinea));
            }
        }

        log.info("Alumnos leidos del CSV: {}", alumnos.size());
        return alumnos;
    }

    private void validarCabecera(String linea) {
        if (!Constantes.CABECERA_CSV.equalsIgnoreCase(linea.trim())) {
            log.warn("La cabecera esperada era '{}' pero se ha encontrado '{}'.", Constantes.CABECERA_CSV, linea);
        }
    }

    private Alumno convertirLineaEnAlumno(String linea, int numeroLinea) throws Exception {
        String[] partes = linea.split(",", -1);

        if (partes.length != 3) {
            throw new Exception("Formato incorrecto en la linea " + numeroLinea + ": " + linea);
        }

        try {
            int id = Integer.parseInt(partes[0].trim());
            String nombre = partes[1].trim();
            double nota = Double.parseDouble(partes[2].trim());

            if (nombre.isEmpty()) {
                throw new Exception("El nombre esta vacio en la linea " + numeroLinea);
            }

            return new Alumno(id, nombre, nota);
        } catch (NumberFormatException e) {
            throw new Exception("Error numerico en la linea " + numeroLinea + ": " + linea, e);
        }
    }
}
