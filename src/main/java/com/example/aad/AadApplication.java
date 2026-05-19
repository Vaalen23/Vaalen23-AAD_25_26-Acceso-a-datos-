package com.example.aad;

import com.example.aad.model.Alumno;
import com.example.aad.repository.CsvAlumnoRepository;
import com.example.aad.service.JsonAlumnoService;
import com.example.aad.service.XmlAlumnoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AadApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AadApplication.class);

    private final CsvAlumnoRepository csvAlumnoRepository;
    private final JsonAlumnoService jsonAlumnoService;
    private final XmlAlumnoService xmlAlumnoService;

    public AadApplication(CsvAlumnoRepository csvAlumnoRepository,
                          JsonAlumnoService jsonAlumnoService,
                          XmlAlumnoService xmlAlumnoService) {
        this.csvAlumnoRepository = csvAlumnoRepository;
        this.jsonAlumnoService = jsonAlumnoService;
        this.xmlAlumnoService = xmlAlumnoService;
    }

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            log.info("Iniciando conversor de formatos CSV -> JSON y XML");

            List<Alumno> alumnos = csvAlumnoRepository.leerTodos();

            if (alumnos.isEmpty()) {
                log.warn("El fichero CSV no contiene alumnos.");
                return;
            }

            jsonAlumnoService.exportar(alumnos);
            xmlAlumnoService.exportar(alumnos);

            log.info("Conversion finalizada correctamente.");
        } catch (Exception e) {
            log.error("No se pudo completar la conversion: {}", e.getMessage());
        }
    }
}
