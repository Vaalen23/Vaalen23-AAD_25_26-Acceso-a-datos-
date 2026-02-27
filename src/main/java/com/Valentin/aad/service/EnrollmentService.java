package com.Valentin.aad.service;

import com.Valentin.aad.config.PostgresqlDriver;
import com.Valentin.aad.repository.EnrollmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    private final PostgresqlDriver driver;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(PostgresqlDriver driver, EnrollmentRepository enrollmentRepository) {
        this.driver = driver;
        this.enrollmentRepository = enrollmentRepository;
    }


    public void enrollStudentInModule(int studentId, int moduleId) {
        try {
            enrollmentRepository.enroll(studentId, moduleId);
            log.info("Matrícula simple OK (student={}, módulo={})", studentId, moduleId);
        } catch (Exception e) {
            log.error("Error en matrícula simple (student={}, módulo={})", studentId, moduleId, e);
            throw e;
        }
    }

    // Ya lo tenías: matricular en DOS módulos con transacción manual
    public void enrollStudentInTwoModules(int studentId, int moduleId1, int moduleId2) {
        driver.beginTransaction();

        try {
            enrollmentRepository.enroll(studentId, moduleId1);
            enrollmentRepository.enroll(studentId, moduleId2);

            driver.commit();
            log.info("Matrícula completada OK (student={}, módulos={}, {})", studentId, moduleId1, moduleId2);

        } catch (Exception e) {
            driver.rollback();
            log.warn("Matrícula falló, rollback hecho (student={}, módulos={}, {})", studentId, moduleId1, moduleId2);
            throw e;
        }
    }
}