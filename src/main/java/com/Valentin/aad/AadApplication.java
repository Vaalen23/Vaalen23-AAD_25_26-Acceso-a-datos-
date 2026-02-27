package com.Valentin.aad;

import com.Valentin.aad.application.StudentManagementService;
import com.Valentin.aad.model.Module;
import com.Valentin.aad.model.Student;
import com.Valentin.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class AadApplication implements CommandLineRunner {

    private final StudentManagementService studentManagementService;
    private final StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Student miriam = new Student(null, "32902598Q", "Valentin",
                "valentin@gmail.com", "DAW", List.of());

        Module programacion = new Module(null, "0485", "Programación", 250);

        miriam = studentManagementService.createStudent(miriam);
        programacion = studentManagementService.createModule(programacion);

        int modulosMatriculados =
                studentManagementService.countEnrollments(miriam.getId());

        // Si en tu modelo es getNombre() en vez de getName(), deja getNombre().
        log.info("{} módulos matriculados para el alumno {}",
                modulosMatriculados, miriam.getNombre());

        studentManagementService.enrollStudentInModule(miriam.getId(),
                programacion.getId());

        studentRepository.delete(miriam.getId());
    }
}