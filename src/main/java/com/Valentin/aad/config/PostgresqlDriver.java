package com.Valentin.aad.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostgresqlDriver {

    private static final Logger log = LoggerFactory.getLogger(PostgresqlDriver.class);

    private final Environment env;

    // Guardamos conexión “activa” solo cuando hay transacción
    private final ThreadLocal<Connection> txConnection = new ThreadLocal<>();

    public PostgresqlDriver(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initDatabase() {
        String url = env.getProperty("spring.datasource.url");
        String user = env.getProperty("spring.datasource.username");
        String pass = env.getProperty("spring.datasource.password");

        List<String> scripts = readScriptListFromYaml();
        log.info("Scripts detectados en YAML: {}", scripts);

        if (scripts.isEmpty()) {
            log.warn("No hay scripts en app.sql.scripts. Revisa application.yml");
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            for (String path : scripts) {
                String sql = loadFromResources(path);
                List<String> statements = splitStatements(sql);

                for (String st : statements) {
                    String trimmed = st.trim();
                    if (trimmed.isEmpty()) continue;

                    try (Statement stmt = con.createStatement()) {
                        stmt.execute(trimmed);
                    }
                }
                log.info("Script ejecutado: {}", path);
            }

            log.info("Database initialized from SQL scripts");
        } catch (Exception e) {
            log.error("Error inicializando la BD desde scripts SQL", e);
        }
    }

    // ✅ Esto lo usarán los repositorios
    public Connection getConnection() throws SQLException {
        Connection currentTx = txConnection.get();
        if (currentTx != null) {
            return currentTx; // si hay transacción, usamos esta conexión
        }

        String url = env.getProperty("spring.datasource.url");
        String user = env.getProperty("spring.datasource.username");
        String pass = env.getProperty("spring.datasource.password");
        return DriverManager.getConnection(url, user, pass);
    }
    public boolean hasActiveTransaction() {
        return txConnection.get() != null;
    }
    public void beginTransaction() {
        if (txConnection.get() != null) {
            throw new IllegalStateException("Ya hay una transacción iniciada");
        }

        try {
            Connection con = getConnection();
            con.setAutoCommit(false);
            txConnection.set(con);
            log.info("Transacción iniciada (autoCommit=false)");
        } catch (Exception e) {
            throw new RuntimeException("Error iniciando transacción", e);
        }
    }

    public void commit() {
        Connection con = txConnection.get();
        if (con == null) {
            throw new IllegalStateException("No hay transacción activa para hacer commit");
        }

        try {
            con.commit();
            log.info("Commit realizado");
        } catch (Exception e) {
            throw new RuntimeException("Error en commit", e);
        } finally {
            closeTxConnection();
        }
    }

    public void rollback() {
        Connection con = txConnection.get();
        if (con == null) {
            log.warn("Rollback llamado pero no había transacción activa");
            return;
        }

        try {
            con.rollback();
            log.info("Rollback realizado");
        } catch (Exception e) {
            throw new RuntimeException("Error en rollback", e);
        } finally {
            closeTxConnection();
        }
    }

    private void closeTxConnection() {
        Connection con = txConnection.get();
        txConnection.remove();

        if (con == null) return;

        try {
            con.setAutoCommit(true);
        } catch (Exception ignored) {}

        try {
            con.close();
        } catch (Exception ignored) {}

        log.info("Conexión de transacción cerrada");
    }

    private List<String> readScriptListFromYaml() {
        List<String> scripts = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String value = env.getProperty("app.sql.scripts[" + i + "]");
            if (value == null) break;
            if (!value.trim().isEmpty()) scripts.add(value.trim());
        }
        return scripts;
    }

    private String loadFromResources(String path) throws Exception {
        var is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (is == null) throw new IllegalArgumentException("No se encontró el script en resources: " + path);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
            return sb.toString();
        }
    }

    private List<String> splitStatements(String sql) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inDollarBlock = false;

        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);

            if (c == '$' && i + 1 < sql.length() && sql.charAt(i + 1) == '$') {
                inDollarBlock = !inDollarBlock;
                current.append("$$");
                i++;
                continue;
            }

            if (c == ';' && !inDollarBlock) {
                result.add(current.toString());
                current.setLength(0);
                continue;
            }

            current.append(c);
        }

        if (!current.toString().trim().isEmpty()) result.add(current.toString());
        return result;
    }
}