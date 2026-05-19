package com.example.aad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

// Configuracion de JdbcTemplate a partir del DataSource autoconfigurado por Spring Boot
@Configuration
public class JdbcConfig {

    // Spring Boot crea el DataSource automaticamente con los datos de application.yml
    // Este bean lo inyecta en JdbcTemplate para usarlo en los repositorios
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
