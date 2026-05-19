package com.example.aad.menu;

import com.example.aad.model.Alumno;
import com.example.aad.model.Matricula;
import com.example.aad.model.Modulo;
import com.example.aad.service.GestionAcademicaService;

import java.util.Scanner;

// Menu interactivo de consola para gestionar alumnos, modulos y matriculas
public class MenuConsola {

    private final GestionAcademicaService servicio;
    private final Scanner scanner = new Scanner(System.in);

    public MenuConsola(GestionAcademicaService servicio) {
        this.servicio = servicio;
    }

    // Punto de entrada del menu principal
    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> menuAlumnos();
                case 2 -> menuModulos();
                case 3 -> menuMatriculas();
                case 0 -> salir = true;
                default -> System.out.println("Opcion no valida.");
            }
        }

        System.out.println("Hasta luego.");
    }

    // ----------------------------------------------------
    // Menu principal
    // ----------------------------------------------------

    private void mostrarMenuPrincipal() {
        System.out.println("\n======================================");
        System.out.println("     SISTEMA DE GESTION ACADEMICA");
        System.out.println("======================================");
        System.out.println("1. Gestion de alumnos");
        System.out.println("2. Gestion de modulos");
        System.out.println("3. Gestion de matriculas");
        System.out.println("0. Salir");
        System.out.println("======================================");
    }

    // ----------------------------------------------------
    // Submenu: alumnos
    // ----------------------------------------------------

    private void menuAlumnos() {
        System.out.println("\n--- Gestion de alumnos ---");
        System.out.println("1. Crear alumno");
        System.out.println("2. Listar alumnos");
        System.out.println("3. Buscar alumno por ID");
        System.out.println("4. Actualizar alumno");
        System.out.println("5. Eliminar alumno");
        System.out.println("0. Volver");

        int opcion = leerEntero("Seleccione una opcion: ");

        switch (opcion) {
            case 1 -> crearAlumno();
            case 2 -> listarAlumnos();
            case 3 -> buscarAlumnoPorId();
            case 4 -> actualizarAlumno();
            case 5 -> eliminarAlumno();
            case 0 -> { }
            default -> System.out.println("Opcion no valida.");
        }
    }

    private void crearAlumno() {
        System.out.println("\n--- Crear alumno ---");
        String nif    = leerTexto("NIF: ");
        String nombre = leerTexto("Nombre: ");
        String email  = leerTexto("Email: ");

        Alumno alumno = new Alumno(null, nif, nombre, email);
        alumno = servicio.crearAlumno(alumno);
        System.out.println("Alumno creado: " + alumno);
    }

    private void listarAlumnos() {
        System.out.println("\n--- Lista de alumnos ---");
        servicio.obtenerTodosLosAlumnos().forEach(System.out::println);
    }

    private void buscarAlumnoPorId() {
        int id = leerEntero("ID del alumno: ");
        Alumno alumno = servicio.obtenerAlumnoPorId(id);
        System.out.println(alumno != null ? alumno : "Alumno no encontrado.");
    }

    private void actualizarAlumno() {
        int id = leerEntero("ID del alumno a actualizar: ");
        Alumno alumno = servicio.obtenerAlumnoPorId(id);

        if (alumno == null) {
            System.out.println("El alumno no existe.");
            return;
        }

        String nombre = leerTexto("Nuevo nombre (" + alumno.getNombre() + "): ");
        String email  = leerTexto("Nuevo email ("  + alumno.getEmail()  + "): ");

        alumno.setNombre(nombre);
        alumno.setEmail(email);
        servicio.actualizarAlumno(alumno);
        System.out.println("Alumno actualizado.");
    }

    private void eliminarAlumno() {
        int id = leerEntero("ID del alumno a eliminar: ");
        servicio.eliminarAlumno(id);
        System.out.println("Alumno eliminado.");
    }

    // ----------------------------------------------------
    // Submenu: modulos
    // ----------------------------------------------------

    private void menuModulos() {
        System.out.println("\n--- Gestion de modulos ---");
        System.out.println("1. Crear modulo");
        System.out.println("2. Listar modulos");
        System.out.println("3. Buscar modulo por ID");
        System.out.println("4. Actualizar modulo");
        System.out.println("5. Eliminar modulo");
        System.out.println("0. Volver");

        int opcion = leerEntero("Seleccione una opcion: ");

        switch (opcion) {
            case 1 -> crearModulo();
            case 2 -> listarModulos();
            case 3 -> buscarModuloPorId();
            case 4 -> actualizarModulo();
            case 5 -> eliminarModulo();
            case 0 -> { }
            default -> System.out.println("Opcion no valida.");
        }
    }

    private void crearModulo() {
        System.out.println("\n--- Crear modulo ---");
        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");
        int horas     = leerEntero("Horas: ");

        Modulo modulo = new Modulo(null, codigo, nombre, horas);
        modulo = servicio.crearModulo(modulo);
        System.out.println("Modulo creado: " + modulo);
    }

    private void listarModulos() {
        System.out.println("\n--- Lista de modulos ---");
        servicio.obtenerTodosLosModulos().forEach(System.out::println);
    }

    private void buscarModuloPorId() {
        int id = leerEntero("ID del modulo: ");
        Modulo modulo = servicio.obtenerModuloPorId(id);
        System.out.println(modulo != null ? modulo : "Modulo no encontrado.");
    }

    private void actualizarModulo() {
        int id = leerEntero("ID del modulo a actualizar: ");
        Modulo modulo = servicio.obtenerModuloPorId(id);

        if (modulo == null) {
            System.out.println("El modulo no existe.");
            return;
        }

        String nombre = leerTexto("Nuevo nombre (" + modulo.getNombre() + "): ");
        int horas     = leerEntero("Nuevas horas (" + modulo.getHoras() + "): ");

        modulo.setNombre(nombre);
        modulo.setHoras(horas);
        servicio.actualizarModulo(modulo);
        System.out.println("Modulo actualizado.");
    }

    private void eliminarModulo() {
        int id = leerEntero("ID del modulo a eliminar: ");
        servicio.eliminarModulo(id);
        System.out.println("Modulo eliminado.");
    }

    // ----------------------------------------------------
    // Submenu: matriculas
    // ----------------------------------------------------

    private void menuMatriculas() {
        System.out.println("\n--- Gestion de matriculas ---");
        System.out.println("1. Matricular alumno en modulo");
        System.out.println("2. Listar todas las matriculas");
        System.out.println("3. Listar matriculas por alumno");
        System.out.println("4. Eliminar matricula");
        System.out.println("5. Contar matriculas de un alumno");
        System.out.println("0. Volver");

        int opcion = leerEntero("Seleccione una opcion: ");

        switch (opcion) {
            case 1 -> matricularAlumno();
            case 2 -> listarMatriculas();
            case 3 -> listarMatriculasPorAlumno();
            case 4 -> eliminarMatricula();
            case 5 -> contarMatriculas();
            case 0 -> { }
            default -> System.out.println("Opcion no valida.");
        }
    }

    private void matricularAlumno() {
        System.out.println("\n--- Matricular alumno ---");
        int idAlumno = leerEntero("ID del alumno: ");
        int idModulo = leerEntero("ID del modulo: ");

        Matricula matricula = servicio.matricularAlumno(idAlumno, idModulo);
        System.out.println("Matricula creada: " + matricula);
    }

    private void listarMatriculas() {
        System.out.println("\n--- Lista de matriculas ---");
        servicio.obtenerTodasLasMatriculas().forEach(System.out::println);
    }

    private void listarMatriculasPorAlumno() {
        int idAlumno = leerEntero("ID del alumno: ");
        servicio.obtenerMatriculasPorAlumno(idAlumno).forEach(System.out::println);
    }

    private void eliminarMatricula() {
        int idAlumno = leerEntero("ID del alumno: ");
        int idModulo = leerEntero("ID del modulo: ");
        servicio.eliminarMatricula(idAlumno, idModulo);
        System.out.println("Matricula eliminada.");
    }

    private void contarMatriculas() {
        int idAlumno = leerEntero("ID del alumno: ");
        int total = servicio.contarMatriculas(idAlumno);
        System.out.println("Total de matriculas: " + total);
    }

    // ----------------------------------------------------
    // Metodos auxiliares de lectura
    // ----------------------------------------------------

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(scanner.nextLine());
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}
