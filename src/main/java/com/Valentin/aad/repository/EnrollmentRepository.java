package com.Valentin.aad.repository;

import com.Valentin.aad.config.PostgresqlDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository
public class EnrollmentRepository {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentRepository.class);

    private final PostgresqlDriver driver;

    public EnrollmentRepository(PostgresqlDriver driver) {
        this.driver = driver;
    }

    public void enroll(int studentId, int moduleId) {
        String sql = "INSERT INTO matricula (id_alumno, id_modulo) VALUES (?, ?)";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = driver.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setInt(2, moduleId);
            ps.executeUpdate();

        } catch (Exception e) {
            log.error("Error matriculando alumno {} en módulo {}", studentId, moduleId, e);
            throw new RuntimeException(e);

        } finally {

            if (ps != null) {
                try { ps.close(); } catch (Exception ignored) {}
            }


            if (con != null && !driver.hasActiveTransaction()) {
                try { con.close(); } catch (Exception ignored) {}
            }
        }
    }
}