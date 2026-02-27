package com.Valentin.aad.repository;

import com.Valentin.aad.config.PostgresqlDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

@Repository
public class EnrollmentStatsRepository {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentStatsRepository.class);

    private final PostgresqlDriver driver;

    public EnrollmentStatsRepository(PostgresqlDriver driver) {
        this.driver = driver;
    }

    public int countEnrollments(int studentId) {
        String sql = "{ ? = call count_enrollments(?) }";

        Connection con = null;
        CallableStatement cs = null;

        try {
            con = driver.getConnection();
            cs = con.prepareCall(sql);

            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, studentId);

            cs.execute();
            return cs.getInt(1);

        } catch (Exception e) {
            log.error("Error llamando a count_enrollments({})", studentId, e);
            throw new RuntimeException(e);

        } finally {
            if (cs != null) {
                try { cs.close(); } catch (Exception ignored) {}
            }

            if (con != null && !driver.hasActiveTransaction()) {
                try { con.close(); } catch (Exception ignored) {}
            }
        }
    }
}