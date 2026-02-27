package com.Valentin.aad.repository;

import com.Valentin.aad.config.PostgresqlDriver;
import com.Valentin.aad.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {

    private static final Logger log = LoggerFactory.getLogger(StudentRepository.class);

    private final PostgresqlDriver driver;

    public StudentRepository(PostgresqlDriver driver) {
        this.driver = driver;
    }

    public Student insert(Student s) {
        String sql = "INSERT INTO alumno (nif, nombre, email, curso) VALUES (?, ?, ?, ?) RETURNING id_alumno";

        try (Connection con = driver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getNif());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getCurso());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s.setId(rs.getInt(1));
                }
            }

            return s;

        } catch (Exception e) {
            log.error("Error insertando alumno", e);
            throw new RuntimeException(e);
        }
    }

    public List<Student> findAll() {
        String sql = "SELECT id_alumno, nif, nombre, email, curso FROM alumno ORDER BY id_alumno";
        List<Student> list = new ArrayList<>();

        try (Connection con = driver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id_alumno"));
                s.setNif(rs.getString("nif"));
                s.setNombre(rs.getString("nombre"));
                s.setEmail(rs.getString("email"));
                s.setCurso(rs.getString("curso"));
                list.add(s);
            }

            return list;

        } catch (Exception e) {
            log.error("Error listando alumnos", e);
            throw new RuntimeException(e);
        }
    }

    public Student findById(int id) {
        String sql = "SELECT id_alumno, nif, nombre, email, curso FROM alumno WHERE id_alumno = ?";

        try (Connection con = driver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Student s = new Student();
                s.setId(rs.getInt("id_alumno"));
                s.setNif(rs.getString("nif"));
                s.setNombre(rs.getString("nombre"));
                s.setEmail(rs.getString("email"));
                s.setCurso(rs.getString("curso"));
                return s;
            }

        } catch (Exception e) {
            log.error("Error buscando alumno por id {}", id, e);
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM alumno WHERE id_alumno = ?";

        try (Connection con = driver.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            log.error("Error borrando alumno id {}", id, e);
            throw new RuntimeException(e);
        }
    }
}