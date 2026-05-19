package com.example.aad.repository;

import com.example.aad.model.Modulo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio que gestiona las operaciones CRUD sobre la tabla modulo usando JdbcTemplate
@Slf4j
@Repository
@RequiredArgsConstructor
public class ModuloRepository {

    private final JdbcTemplate jdbcTemplate;

    // Inserta un modulo y devuelve la entidad con el id generado
    public Modulo insertar(Modulo modulo) {
        String sql = """
                INSERT INTO modulo (codigo, nombre, horas)
                VALUES (?, ?, ?)
                RETURNING id_modulo
                """;
        try {
            Integer id = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    modulo.getCodigo(),
                    modulo.getNombre(),
                    modulo.getHoras()
            );
            modulo.setId(id);
            log.info("Modulo insertado con id: {}", id);
            return modulo;

        } catch (Exception e) {
            log.error("Error al insertar modulo: {}", e.getMessage());
            throw new RuntimeException("Error al insertar modulo", e);
        }
    }

    // Devuelve todos los modulos de la base de datos
    public List<Modulo> buscarTodos() {
        String sql = "SELECT id_modulo, codigo, nombre, horas FROM modulo";
        try {
            List<Modulo> lista = jdbcTemplate.query(sql, (rs, rowNum) ->
                    new Modulo(
                            rs.getInt("id_modulo"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("horas")
                    )
            );
            log.info("Modulos encontrados: {}", lista.size());
            return lista;

        } catch (Exception e) {
            log.error("Error al obtener la lista de modulos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener modulos", e);
        }
    }

    // Busca un modulo por su id. Devuelve null si no existe
    public Modulo buscarPorId(int id) {
        String sql = "SELECT id_modulo, codigo, nombre, horas FROM modulo WHERE id_modulo = ?";
        try {
            return jdbcTemplate.query(sql,
                    (rs, rowNum) -> new Modulo(
                            rs.getInt("id_modulo"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("horas")
                    ), id
            ).stream().findFirst().orElse(null);

        } catch (Exception e) {
            log.error("Error al buscar modulo con id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar modulo", e);
        }
    }

    // Busca un modulo por su codigo unico. Devuelve null si no existe
    public Modulo buscarPorCodigo(String codigo) {
        String sql = "SELECT id_modulo, codigo, nombre, horas FROM modulo WHERE codigo = ?";
        try {
            return jdbcTemplate.query(sql,
                    (rs, rowNum) -> new Modulo(
                            rs.getInt("id_modulo"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("horas")
                    ), codigo
            ).stream().findFirst().orElse(null);

        } catch (Exception e) {
            log.error("Error al buscar modulo por codigo {}: {}", codigo, e.getMessage());
            throw new RuntimeException("Error al buscar modulo por codigo", e);
        }
    }

    // Actualiza los datos de un modulo existente
    public Modulo actualizar(Modulo modulo) {
        String sql = """
                UPDATE modulo
                SET codigo = ?, nombre = ?, horas = ?
                WHERE id_modulo = ?
                """;
        try {
            jdbcTemplate.update(sql,
                    modulo.getCodigo(),
                    modulo.getNombre(),
                    modulo.getHoras(),
                    modulo.getId()
            );
            log.info("Modulo actualizado: {}", modulo.getId());
            return modulo;

        } catch (Exception e) {
            log.error("Error al actualizar modulo {}: {}", modulo.getId(), e.getMessage());
            throw new RuntimeException("Error al actualizar modulo", e);
        }
    }

    // Elimina un modulo por su id
    public void eliminar(int id) {
        String sql = "DELETE FROM modulo WHERE id_modulo = ?";
        try {
            jdbcTemplate.update(sql, id);
            log.info("Modulo eliminado: {}", id);

        } catch (Exception e) {
            log.error("Error al eliminar modulo {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar modulo", e);
        }
    }

    // Comprueba si existe un modulo con el codigo indicado
    public boolean existePorCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM modulo WHERE codigo = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, codigo);
            return count != null && count > 0;

        } catch (Exception e) {
            log.error("Error al comprobar existencia de codigo {}: {}", codigo, e.getMessage());
            return false;
        }
    }
}
