package com.Valentin.aad;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ACT1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Mini Explorador (simple). Deja vacío para salir.");

        // pedir ruta de directorio
        System.out.print("Ruta del directorio: ");
        String entrada = sc.nextLine().trim();
        if (entrada.isEmpty()) return;

        Path dir = Paths.get(entrada).toAbsolutePath().normalize();
        if (!Files.isDirectory(dir)) {
            System.out.println("No es un directorio válido.");
            return;
        }

        // listar una vez
        listar(dir);

        // menú básico
        while (true) {
            System.out.println("\n1) Crear  2) Mover  3) Borrar  4) Listar  0) Salir");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();

            if ("0".equals(op)) {
                System.out.println("Adiós!");
                break;
            } else if ("1".equals(op)) {
                // crear fichero vacío
                System.out.print("Nombre o ruta del nuevo fichero: ");
                String n = sc.nextLine().trim();
                if (n.isEmpty()) continue;

                Path t = Paths.get(n);
                if (!t.isAbsolute()) t = dir.resolve(t).toAbsolutePath().normalize();

                try {
                    if (t.getParent() != null) Files.createDirectories(t.getParent());
                    Files.createFile(t);
                    System.out.println("Creado: " + t);
                } catch (FileAlreadyExistsException e) {
                    System.out.println("Ya existe ese fichero.");
                } catch (AccessDeniedException e) {
                    System.out.println("Sin permisos para crear ahí.");
                } catch (IOException e) {
                    System.out.println("No se pudo crear: " + e.getMessage());
                }

            } else if ("2".equals(op)) {
                // mover fichero
                System.out.print("Ruta origen: ");
                String o = sc.nextLine().trim();
                System.out.print("Ruta destino (con nombre): ");
                String d = sc.nextLine().trim();
                if (o.isEmpty() || d.isEmpty()) continue;

                Path po = Paths.get(o); if (!po.isAbsolute()) po = dir.resolve(po).toAbsolutePath().normalize();
                Path pd = Paths.get(d); if (!pd.isAbsolute()) pd = dir.resolve(pd).toAbsolutePath().normalize();

                try {
                    if (pd.getParent() != null) Files.createDirectories(pd.getParent());
                    try {
                        Files.move(po, pd, StandardCopyOption.ATOMIC_MOVE); // primero atómico
                    } catch (AtomicMoveNotSupportedException e) {
                        Files.move(po, pd); // si no se puede, normal
                    }
                    System.out.println("Movido: " + po + " -> " + pd);
                } catch (NoSuchFileException e) {
                    System.out.println("El origen no existe.");
                } catch (AccessDeniedException e) {
                    System.out.println("Sin permisos para mover.");
                } catch (FileAlreadyExistsException e) {
                    System.out.println("El destino ya existe.");
                } catch (IOException e) {
                    System.out.println("No se pudo mover: " + e.getMessage());
                }

            } else if ("3".equals(op)) {
                // borrar fichero
                System.out.print("Ruta del fichero a borrar: ");
                String b = sc.nextLine().trim();
                if (b.isEmpty()) continue;

                Path pb = Paths.get(b); if (!pb.isAbsolute()) pb = dir.resolve(pb).toAbsolutePath().normalize();

                try {
                    boolean ok = Files.deleteIfExists(pb);
                    System.out.println(ok ? "Borrado." : "No existía.");
                } catch (AccessDeniedException e) {
                    System.out.println("Sin permisos para borrar.");
                } catch (IOException e) {
                    System.out.println("No se pudo borrar: " + e.getMessage());
                }

            } else if ("4".equals(op)) {
                // listar de nuevo
                listar(dir);
            } else {
                System.out.println("Opción inválida.");
            }
        }
    }

    // lista el contenido del directorio (carpetas y ficheros)
    private static void listar(Path dir) {
        System.out.println("\n--- Contenido de " + dir + " ---");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path p : ds) {
                try {
                    BasicFileAttributes a = Files.readAttributes(p, BasicFileAttributes.class);
                    if (a.isDirectory()) {
                        System.out.println("[DIR ] " + p.getFileName());
                    } else if (a.isRegularFile()) {
                        String fecha = sdf.format(new Date(a.lastModifiedTime().toMillis()));
                        System.out.println("[FILE] " + p.getFileName() + " | " + a.size() + " bytes | " + fecha);
                    } else {
                        System.out.println("[OTRO] " + p.getFileName());
                    }
                } catch (IOException e) {
                    System.out.println("[??? ] " + p.getFileName());
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo listar: " + e.getMessage());
        }
    }
}
