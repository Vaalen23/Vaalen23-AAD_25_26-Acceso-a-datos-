package com.Valentin.aad.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DbPing implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DbPing.class);

    private final Environment env;

    public DbPing(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String url = env.getProperty("spring.datasource.url");
        String user = env.getProperty("spring.datasource.username");
        String pass = env.getProperty("spring.datasource.password");

        log.info("Probando conexión JDBC a {}", url);

        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1")) {

            if (rs.next()) {
                int one = rs.getInt(1);
                log.info("Conexión OK. SELECT 1 devolvió {}", one);
            } else {
                log.warn("Conexión OK, pero SELECT 1 no devolvió filas (raro).");
            }

        } catch (Exception e) {
            log.error("Fallo conectando a la BD. Revisa Docker/puerto/credenciales.", e);
        }
    }
}