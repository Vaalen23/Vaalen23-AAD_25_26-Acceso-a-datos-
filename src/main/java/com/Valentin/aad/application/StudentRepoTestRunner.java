package com.Valentin.aad.application;

import com.Valentin.aad.model.Module;
import com.Valentin.aad.model.Student;
import com.Valentin.aad.repository.EnrollmentStatsRepository;
import com.Valentin.aad.repository.ModuleRepository;
import com.Valentin.aad.repository.StudentRepository;
import com.Valentin.aad.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class StudentRepoTestRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StudentRepoTestRunner.class);

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentService enrollmentService;
    private final EnrollmentStatsRepository statsRepository;

    public StudentRepoTestRunner(
            StudentRepository studentRepository,
            ModuleRepository moduleRepository,
            EnrollmentService enrollmentService,
            EnrollmentStatsRepository statsRepository
    ) {
        this.studentRepository = studentRepository;
        this.moduleRepository = moduleRepository;
        this.enrollmentService = enrollmentService;
        this.statsRepository = statsRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Student s = new Student(null, "99999999X", "Valentin", "valen@test.com", "2DAM", List.of());
        studentRepository.insert(s);
        log.info("Alumno insertado con id: {}", s.getId());

        Module m1 = new Module(null, "PROG", "Programación", 256);
        moduleRepository.insert(m1);
        log.info("Módulo insertado con id: {}", m1.getId());

        Module m2 = new Module(null, "AAD", "Acceso a Datos", 192);
        moduleRepository.insert(m2);

        // Matriculamos al alumno en 2 módulos (con transacción)
        enrollmentService.enrollStudentInTwoModules(s.getId(), m1.getId(), m2.getId());

        // Llamada a función almacenada
        int total = statsRepository.countEnrollments(s.getId());
        log.info("Total matrículas del alumno {}: {}", s.getId(), total);
    }
}