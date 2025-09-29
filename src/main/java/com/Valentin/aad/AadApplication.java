package com.Valentin.aad;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class AadApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AadApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        File fichero = new File("ejemplo.txt");
        if (fichero.createNewFile()) {
            System.out.println("Fichero creado: " + fichero.getName());
        } else {
            System.out.println("El fichero ya existe."); //¿cómo se dónde está?
        }
    }
}
