package com.example.aad;

import com.example.aad.service.CodificacionService;
import com.example.aad.service.LogService;
import com.example.aad.util.Constantes;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
public class AadApplication implements CommandLineRunner {

    private final LogService logService;
    private final CodificacionService codificacionService;
    private Charset charset = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String opcion;
            do {
                mostrarMenu();
                opcion = scanner.nextLine();
                ejecutarOpcion(opcion, scanner);
            } while (!"0".equals(opcion));
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("=== GESTOR DE LOGS ===");
        System.out.println("Codificacion actual: " + codificacionService.obtenerNombre(charset));
        System.out.println("1. Anadir evento");
        System.out.println("2. Filtrar eventos por fecha");
        System.out.println("3. Mostrar todos los eventos");
        System.out.println("4. Cambiar codificacion");
        System.out.println("5. Cargar eventos de ejemplo");
        System.out.println("0. Salir");
        System.out.print("Elige una opcion: ");
    }

    private void ejecutarOpcion(String opcion, Scanner scanner) {
        switch (opcion) {
            case "1" -> anadirEvento(scanner);
            case "2" -> filtrarPorFecha(scanner);
            case "3" -> logService.mostrarTodos(charset);
            case "4" -> cambiarCodificacion(scanner);
            case "5" -> logService.cargarEventosEjemplo(charset);
            case "0" -> System.out.println("Aplicacion finalizada.");
            default -> System.out.println("Opcion no valida.");
        }
    }

    private void anadirEvento(Scanner scanner) {
        System.out.print("Introduce el mensaje del evento: ");
        String mensaje = scanner.nextLine();
        if (mensaje.isBlank()) {
            System.out.println("El mensaje no puede estar vacio.");
            return;
        }
        logService.anadirEvento(mensaje, charset);
    }

    private void filtrarPorFecha(Scanner scanner) {
        System.out.print("Introduce una fecha (YYYY-MM-DD): ");
        String textoFecha = scanner.nextLine();
        try {
            LocalDate fecha = LocalDate.parse(textoFecha, Constantes.FORMATO_FECHA);
            logService.filtrarPorFecha(fecha, charset);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha incorrecto. Usa YYYY-MM-DD.");
        }
    }

    private void cambiarCodificacion(Scanner scanner) {
        System.out.println("1. UTF-8");
        System.out.println("2. ISO-8859-1");
        System.out.print("Elige codificacion: ");
        charset = codificacionService.obtenerCodificacion(scanner.nextLine());
        System.out.println("Codificacion cambiada a " + codificacionService.obtenerNombre(charset));
    }
}
