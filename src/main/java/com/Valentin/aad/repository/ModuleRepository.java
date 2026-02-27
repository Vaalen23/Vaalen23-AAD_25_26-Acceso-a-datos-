package com.Valentin.aad.repository;

import com.Valentin.aad.config.PostgresqlDriver;
import com.Valentin.aad.model.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ModuleRepository {

    private static final Logger log = LoggerFactory.getLogger(ModuleRepository.class);

    private final PostgresqlDriver driver;

    public ModuleRepository(PostgresqlDriver driver) {
        this.driver = driver;
    }

    public Module insert(Module m) {
        String sql = "INSERT INTO modulo (codigo, nombre, horas) VALUES (?, ?, ?) RETURNING id_modulo";

        try (Connection con = driver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getCodigo());
            ps.setString(2, m.getNombre());

            if (m.getHoras() == null) ps.setNull(3, java.sql.Types.INTEGER);
            else ps.setInt(3, m.getHoras());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m.setId(rs.getInt(1));
                }
            }

            return m;

        } catch (Exception e) {
            log.error("Error insertando módulo", e);
            throw new RuntimeException(e);
        }
    }

    public List<Module> findAll() {
        String sql = "SELECT id_modulo, codigo, nombre, horas FROM modulo ORDER BY id_modulo";
        List<Module> list = new ArrayList<>();

        try (Connection con = driver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Module m = new Module();
                m.setId(rs.getInt("id_modulo"));
                m.setCodigo(rs.getString("codigo"));
                m.setNombre(rs.getString("nombre"));

                int horas = rs.getInt("horas");
                if (rs.wasNull()) m.setHoras(null);
                else m.setHoras(horas);

                list.add(m);
            }

            return list;

        } catch (Exception e) {
            log.error("Error listando módulos", e);
            throw new RuntimeException(e);
        }
    }

    public Module findById(int id) {
        String sql = "SELECT id_modulo, codigo, nombre, horas FROM modulo WHERE id_modulo = ?";

        try (Connection con = driver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Module m = new Module();
                m.setId(rs.getInt("id_modulo"));
                m.setCodigo(rs.getString("codigo"));
                m.setNombre(rs.getString("nombre"));

                int horas = rs.getInt("horas");
                if (rs.wasNull()) m.setHoras(null);
                else m.setHoras(horas);

                return m;
            }

        } catch (Exception e) {
            log.error("Error buscando módulo id {}", id, e);
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM modulo WHERE id_modulo = ?";

        try (Connection con = driver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            log.error("Error borrando módulo id {}", id, e);
            throw new RuntimeException(e);
        }
    }
}