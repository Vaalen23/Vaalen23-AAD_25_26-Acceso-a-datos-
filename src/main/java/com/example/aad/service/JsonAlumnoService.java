package com.example.aad.service;

import com.example.aad.model.Alumno;
import com.example.aad.util.Constantes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class JsonAlumnoService implements ConversorService<Alumno> {

    private static final Logger log = LoggerFactory.getLogger(JsonAlumnoService.class);

    @Override
    public void exportar(List<Alumno> alumnos) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(Constantes.FICHERO_JSON), alumnos);

        log.info("Fichero {} generado correctamente.", Constantes.FICHERO_JSON);
    }
}
