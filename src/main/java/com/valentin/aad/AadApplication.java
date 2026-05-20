package com.valentin.aad;

import com.valentin.aad.model.Enrollment;
import com.valentin.aad.model.Module;
import com.valentin.aad.model.Profile;
import com.valentin.aad.model.Student;
import com.valentin.aad.repository.StudentRepository;
import com.valentin.aad.service.EnrollmentService;
import com.valentin.aad.service.StudentManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class AadApplication {

    public static void main(String[] args) {
        SpringApplication.run(AadApplication.class, args);
    }
}

@Component
@RequiredArgsConstructor
@Slf4j
class AppRunner implements CommandLineRunner {

    private final StudentManagementService managementService;
    private final EnrollmentService enrollmentService;
    private final StudentRepository studentRepository;

    @Override

    public void run(String... args) {
        Profile valentinProfile = new Profile("Calle Rio Tinto 8", "600112233");
        Profile vitoProfile = new Profile("Avenida Andalucia 15", "611223344");
        Profile kikoProfile = new Profile("Calle Marisma 4", "622334455");
        Profile pacoProfile = new Profile("Plaza del Puerto 2", "633445566");
        Profile carlosProfile = new Profile("Calle Enebro 19", "644556677");

        Student valentin = new Student("66280457T", "Valentin", "valentin@g.educaand.es", "DAM");
        valentin.setProfile(valentinProfile);

        Student vito = new Student("11111111A", "vito", "vito@g.educaand.es", "DAM");
        vito.setProfile(vitoProfile);

        Student kiko = new Student("22222222B", "kiko", "kiko@g.educaand.es", "DAM");
        kiko.setProfile(kikoProfile);

        Student paco = new Student("33333333C", "paco", "paco@g.educaand.es", "DAM");
        paco.setProfile(pacoProfile);

        Student carlos = new Student("44444444D", "carlos", "carlos@g.educaand.es", "DAM");
        carlos.setProfile(carlosProfile);

        Module accesoDatos = new Module("AD", "Acceso a Datos", 180);
        Module interfaces = new Module("DI", "Desarrollo de Interfaces", 120);
        Module servicios = new Module("PSP", "Programacion de Servicios", 100);

        valentin = managementService.createStudent(valentin);
        vito = managementService.createStudent(vito);
        kiko = managementService.createStudent(kiko);
        paco = managementService.createStudent(paco);
        carlos = managementService.createStudent(carlos);

        accesoDatos = managementService.createModule(accesoDatos);
        interfaces = managementService.createModule(interfaces);
        servicios = managementService.createModule(servicios);

        Enrollment e1 = managementService.enrollStudentInModule(valentin.getId(), accesoDatos.getId(), 8.5);
        Enrollment e2 = managementService.enrollStudentInModule(vito.getId(), accesoDatos.getId(), 7.0);
        Enrollment e3 = managementService.enrollStudentInModule(kiko.getId(), interfaces.getId(), 6.5);
        Enrollment e4 = managementService.enrollStudentInModule(paco.getId(), servicios.getId(), 9.0);
        Enrollment e5 = managementService.enrollStudentInModule(carlos.getId(), accesoDatos.getId(), 5.5);

        log.info("Alumno creado: {}", valentin);
        log.info("Modulo creado: {}", accesoDatos);
        log.info("Matriculas creadas: {}, {}, {}, {}, {}", e1.getId(), e2.getId(), e3.getId(), e4.getId(), e5.getId());

        int countEnrollments = managementService.countEnrollments(valentin.getId());
        log.info("{} modulos matriculados para el alumno {}", countEnrollments, valentin.getName());

        Student recovered = studentRepository.findByNif(valentin.getNif())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        log.info("Alumno recuperado por NIF: {}", recovered);

        log.info("Busqueda por email educaand: {}", managementService.searchStudentsByEmail("educaand").size());
        log.info("Notas altas: {}", enrollmentService.findHighGrades(8.0).size());
        log.info("Media del modulo AD: {}", enrollmentService.averageGradeByModule(accesoDatos.getId()));

        try {
            enrollmentService.enrollStudentWithError(valentin.getId(), servicios.getId());
        } catch (RuntimeException ex) {
            log.info("Rollback probado correctamente: {}", ex.getMessage());
        }
    }
}
