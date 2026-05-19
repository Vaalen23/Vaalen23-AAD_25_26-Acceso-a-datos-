package com.example.aad;

import com.example.aad.menu.MenuConsola;
import com.example.aad.service.GestionAcademicaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Clase principal de la aplicacion de gestion academica
@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class AadApplication implements CommandLineRunner {

    private final GestionAcademicaService gestionAcademicaService;

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Aplicacion iniciada correctamente.");
        MenuConsola menu = new MenuConsola(gestionAcademicaService);
        menu.iniciar();
    }
}
