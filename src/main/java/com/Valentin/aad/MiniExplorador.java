package com.Valentin.aad;

import java.io.File;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

public class MiniExplorador {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Primero pedimos al usuario una carpeta
        System.out.print("Escribe la ruta de una carpeta: ");
        String ruta = sc.nextLine();

        File carpeta = new File(ruta);

        if (carpeta.exists() && carpeta.isDirectory()) {

            mostrarContenido(carpeta);

            int opcion = 0;

            do {
                System.out.println("\n===== MINI EXPLORADOR =====");
                System.out.println("1) Crear fichero vacío");
                System.out.println("2) Mover fichero");
                System.out.println("3) Borrar fichero");
                System.out.println("4) Salir");
                System.out.print("Elige opción: ");

                if (!sc.hasNextInt()) {
                    System.out.println("Eso no es un número, inténtalo otra vez.");
                    sc.nextLine(); // limpiar basura
                    continue;
                }

                opcion = sc.nextInt();
                sc.nextLine(); // limpiar salto de línea

                switch (opcion) {
                    case 1:
                        crearFichero(carpeta, sc);
                        mostrarContenido(carpeta);
                        break;
                    case 2:
                        moverFichero(carpeta, sc);
                        mostrarContenido(carpeta);
                        break;
                    case 3:
                        borrarFichero(carpeta, sc);
                        mostrarContenido(carpeta);
                        break;
                    case 4:
                        System.out.println("Cerrando el explorador...");
                        break;
                    default:
                        System.out.println("Opción inexistente, prueba con 1, 2, 3 o 4.");
                }

            } while (opcion != 4);

        } else {
            // Si la ruta no es válida avisamos y ya
            System.out.println("Esa ruta no existe o no es una carpeta válida.");
        }

        sc.close();
    }

    // Enseña lo que hay dentro de la carpeta
    private static void mostrarContenido(File carpeta) {
        System.out.println("\nContenido de: " + carpeta.getAbsolutePath());

        File[] lista = carpeta.listFiles();

        if (lista != null && lista.length > 0) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (File f : lista) {
                if (f.isDirectory()) {
                    System.out.println("[CARPETA] " + f.getName());
                } else {
                    long tam = f.length();
                    String fecha = formato.format(new Date(f.lastModified()));
                    System.out.println("[FICHERO] " + f.getName() + " (" + tam + " bytes) - Modificado: " + fecha);
                }
            }
        } else {
            System.out.println("No hay nada dentro (carpeta vacía).");
        }
    }

    // Crea un archivo vacío en la carpeta
    private static void crearFichero(File carpeta, Scanner sc) {
        System.out.print("Nombre del fichero nuevo (con extensión): ");
        String nombre = sc.nextLine();

        File nuevo = new File(carpeta, nombre);

        try {
            if (nuevo.createNewFile()) {
                System.out.println("Fichero creado: " + nuevo.getAbsolutePath());
            } else {
                System.out.println("Ya existe un fichero con ese nombre.");
            }
        } catch (IOException e) {
            System.out.println("No se pudo crear el fichero: " + e.getMessage());
        }
    }

    // Mueve un archivo de la carpeta a otra ruta
    private static void moverFichero(File carpeta, Scanner sc) {
        System.out.print("Nombre del fichero a mover: ");
        String nombreMover = sc.nextLine();

        File origen = new File(carpeta, nombreMover);

        if (origen.exists() && origen.isFile()) {
            System.out.print("Ruta de la carpeta de destino: ");
            String rutaDestino = sc.nextLine();

            File carpetaDestino = new File(rutaDestino);

            if (carpetaDestino.exists() && carpetaDestino.isDirectory()) {
                try {
                    Path origenPath = origen.toPath();
                    Path destinoPath = Path.of(carpetaDestino.getAbsolutePath(), origen.getName());

                    Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);

                    System.out.println("Fichero movido a: " + destinoPath.toString());
                } catch (IOException e) {
                    System.out.println("Error moviendo el fichero: " + e.getMessage());
                }
            } else {
                System.out.println("La carpeta de destino no es válida.");
            }
        } else {
            System.out.println("Ese fichero no existe en la carpeta actual.");
        }
    }

    // Borra un archivo dentro de la carpeta
    private static void borrarFichero(File carpeta, Scanner sc) {
        System.out.print("Nombre del fichero a borrar: ");
        String nombreBorrar = sc.nextLine();

        File fichero = new File(carpeta, nombreBorrar);

        if (fichero.exists() && fichero.isFile()) {
            boolean ok = fichero.delete();
            if (ok) {
                System.out.println("Fichero borrado: " + fichero.getName());
            } else {
                System.out.println("No se pudo borrar (puede estar en uso o protegido).");
            }
        } else {
            System.out.println("No he encontrado ese fichero en la carpeta.");
        }
    }
}
