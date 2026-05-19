package com.example.aad.repository;

import com.example.aad.model.Alumno;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio que gestiona las operaciones CRUD sobre la tabla alumno usando JdbcTemplate
@Slf4j
@Repository
@RequiredArgsConstructor
public class AlumnoRepository {

    private final JdbcTemplate jdbcTemplate;

    // Inserta un alumno y devuelve la entidad con el id generado
    public Alumno insertar(Alumno alumno) {
        String sql = """
                INSERT INTO alumno (nif, nombre, email)
                VALUES (?, ?, ?)
                RETURNING id_alumno
                """;
        try {
            Integer id = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    alumno.getNif(),
                    alumno.getNombre(),
                    alumno.getEmail()
            );
            alumno.setId(id);
            log.info("Alumno insertado con id: {}", id);
            return alumno;

        } catch (Exception e) {
            log.error("Error al insertar alumno: {}", e.getMessage());
            throw new RuntimeException("Error al insertar alumno", e);
        }
    }

    // Devuelve todos los alumnos de la base de datos
    public List<Alumno> buscarTodos() {
        String sql = "SELECT id_alumno, nif, nombre, email FROM alumno";
        try {
            List<Alumno> lista = jdbcTemplate.query(sql, (rs, rowNum) ->
                    new Alumno(
                            rs.getInt("id_alumno"),
                            rs.getString("nif"),
                            rs.getString("nombre"),
                            rs.getString("email")
                    )
            );
            log.info("Alumnos encontrados: {}", lista.size());
            return lista;

        } catch (Exception e) {
            log.error("Error al obtener la lista de alumnos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener alumnos", e);
        }
    }

    // Busca un alumno por su id. Devuelve null si no existe
    public Alumno buscarPorId(int id) {
        String sql = "SELECT id_alumno, nif, nombre, email FROM alumno WHERE id_alumno = ?";
        try {
            return jdbcTemplate.query(sql,
                    (rs, rowNum) -> new Alumno(
                            rs.getInt("id_alumno"),
                            rs.getString("nif"),
                            rs.getString("nombre"),
                            rs.getString("email")
                    ), id
            ).stream().findFirst().orElse(null);

        } catch (Exception e) {
            log.error("Error al buscar alumno con id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar alumno", e);
        }
    }

    // Busca un alumno por su NIF. Devuelve null si no existe
    public Alumno buscarPorNif(String nif) {
        String sql = "SELECT id_alumno, nif, nombre, email FROM alumno WHERE nif = ?";
        try {
            return jdbcTemplate.query(sql,
                    (rs, rowNum) -> new Alumno(
                            rs.getInt("id_alumno"),
                            rs.getString("nif"),
                            rs.getString("nombre"),
                            rs.getString("email")
                    ), nif
            ).stream().findFirst().orElse(null);

        } catch (Exception e) {
            log.error("Error al buscar alumno por NIF {}: {}", nif, e.getMessage());
            throw new RuntimeException("Error al buscar alumno por NIF", e);
        }
    }

    // Actualiza los datos de un alumno existente
    public Alumno actualizar(Alumno alumno) {
        String sql = """
                UPDATE alumno
                SET nif = ?, nombre = ?, email = ?
                WHERE id_alumno = ?
                """;
        try {
            jdbcTemplate.update(sql,
                    alumno.getNif(),
                    alumno.getNombre(),
                    alumno.getEmail(),
                    alumno.getId()
            );
            log.info("Alumno actualizado: {}", alumno.getId());
            return alumno;

        } catch (Exception e) {
            log.error("Error al actualizar alumno {}: {}", alumno.getId(), e.getMessage());
            throw new RuntimeException("Error al actualizar alumno", e);
        }
    }

    // Elimina un alumno por su id
    public void eliminar(int id) {
        String sql = "DELETE FROM alumno WHERE id_alumno = ?";
        try {
            jdbcTemplate.update(sql, id);
            log.info("Alumno eliminado: {}", id);

        } catch (Exception e) {
            log.error("Error al eliminar alumno {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar alumno", e);
        }
    }

    // Comprueba si existe un alumno con el NIF indicado
    public boolean existePorNif(String nif) {
        String sql = "SELECT COUNT(*) FROM alumno WHERE nif = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nif);
            return count != null && count > 0;

        } catch (Exception e) {
            log.error("Error al comprobar existencia de NIF {}: {}", nif, e.getMessage());
            return false;
        }
    }
}
