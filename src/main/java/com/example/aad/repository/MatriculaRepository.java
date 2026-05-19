package com.example.aad.repository;

import com.example.aad.model.Matricula;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

// Repositorio que gestiona la tabla matricula con JdbcTemplate y SimpleJdbcCall
@Slf4j
@Repository
@RequiredArgsConstructor
public class MatriculaRepository {

    private final JdbcTemplate jdbcTemplate;

    // Llamada a la funcion almacenada count_enrollments
    private SimpleJdbcCall contarMatriculasFn;

    // Inicializa el SimpleJdbcCall una sola vez al arrancar
    @PostConstruct
    private void init() {
        this.contarMatriculasFn = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("count_enrollments");
    }

    // Inserta una nueva matricula
    public Matricula insertar(Matricula matricula) {
        String sql = """
                INSERT INTO matricula (id_alumno, id_modulo, fecha)
                VALUES (?, ?, ?)
                """;
        try {
            jdbcTemplate.update(sql,
                    matricula.getIdAlumno(),
                    matricula.getIdModulo(),
                    Date.valueOf(matricula.getFecha())
            );
            log.info("Matricula creada: alumno={} modulo={}", matricula.getIdAlumno(), matricula.getIdModulo());
            return matricula;

        } catch (Exception e) {
            log.error("Error al insertar matricula: {}", e.getMessage());
            throw new RuntimeException("Error al insertar matricula", e);
        }
    }

    // Devuelve todas las matriculas registradas
    public List<Matricula> buscarTodas() {
        String sql = "SELECT id_alumno, id_modulo, fecha FROM matricula";
        try {
            List<Matricula> lista = jdbcTemplate.query(sql, (rs, rowNum) ->
                    new Matricula(
                            rs.getInt("id_alumno"),
                            rs.getInt("id_modulo"),
                            rs.getDate("fecha").toLocalDate()
                    )
            );
            log.info("Matriculas encontradas: {}", lista.size());
            return lista;

        } catch (Exception e) {
            log.error("Error al obtener matriculas: {}", e.getMessage());
            throw new RuntimeException("Error al obtener matriculas", e);
        }
    }

    // Devuelve todas las matriculas de un alumno concreto
    public List<Matricula> buscarPorAlumno(int idAlumno) {
        String sql = """
                SELECT id_alumno, id_modulo, fecha
                FROM matricula
                WHERE id_alumno = ?
                """;
        try {
            List<Matricula> lista = jdbcTemplate.query(sql,
                    (rs, rowNum) -> new Matricula(
                            rs.getInt("id_alumno"),
                            rs.getInt("id_modulo"),
                            rs.getDate("fecha").toLocalDate()
                    ), idAlumno
            );
            log.info("Matriculas del alumno {}: {}", idAlumno, lista.size());
            return lista;

        } catch (Exception e) {
            log.error("Error al obtener matriculas del alumno {}: {}", idAlumno, e.getMessage());
            throw new RuntimeException("Error al obtener matriculas por alumno", e);
        }
    }

    // Elimina una matricula por su clave compuesta
    public void eliminar(int idAlumno, int idModulo) {
        String sql = """
                DELETE FROM matricula
                WHERE id_alumno = ? AND id_modulo = ?
                """;
        try {
            jdbcTemplate.update(sql, idAlumno, idModulo);
            log.info("Matricula eliminada: alumno={} modulo={}", idAlumno, idModulo);

        } catch (Exception e) {
            log.error("Error al eliminar matricula alumno={} modulo={}: {}", idAlumno, idModulo, e.getMessage());
            throw new RuntimeException("Error al eliminar matricula", e);
        }
    }

    // Comprueba si ya existe una matricula para el alumno y modulo indicados
    public boolean existe(int idAlumno, int idModulo) {
        String sql = """
                SELECT COUNT(*)
                FROM matricula
                WHERE id_alumno = ? AND id_modulo = ?
                """;
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idAlumno, idModulo);
            return count != null && count > 0;

        } catch (Exception e) {
            log.error("Error al comprobar existencia de matricula: {}", e.getMessage());
            return false;
        }
    }

    // Llama a la funcion almacenada count_enrollments mediante SimpleJdbcCall
    public int contarMatriculas(int idAlumno) {
        try {
            Map<String, Object> resultado = contarMatriculasFn.execute(idAlumno);
            Integer total = (Integer) resultado.get("returnvalue");
            log.info("El alumno {} tiene {} matriculas", idAlumno, total);
            return total != null ? total : 0;

        } catch (Exception e) {
            log.error("Error al llamar a count_enrollments para alumno {}: {}", idAlumno, e.getMessage());
            throw new RuntimeException("Error al contar matriculas", e);
        }
    }
}
