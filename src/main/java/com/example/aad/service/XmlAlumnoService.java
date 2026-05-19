package com.example.aad.service;

import com.example.aad.model.Alumno;
import com.example.aad.model.Alumnos;
import com.example.aad.util.Constantes;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class XmlAlumnoService implements ConversorService<Alumno> {

    private static final Logger log = LoggerFactory.getLogger(XmlAlumnoService.class);

    @Override
    public void exportar(List<Alumno> alumnos) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();

        xmlMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(Constantes.FICHERO_XML), new Alumnos(alumnos));

        log.info("Fichero {} generado correctamente.", Constantes.FICHERO_XML);
    }
}
