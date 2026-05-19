package com.example.aad.service;

import com.example.aad.model.Alumno;
import com.example.aad.model.Matricula;
import com.example.aad.model.Modulo;
import com.example.aad.repository.AlumnoRepository;
import com.example.aad.repository.MatriculaRepository;
import com.example.aad.repository.ModuloRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// Servicio principal que coordina las operaciones del sistema academico
// Las operaciones de escritura usan @Transactional para garantizar la integridad
@Slf4j
@Service
@RequiredArgsConstructor
public class GestionAcademicaService {

    private final AlumnoRepository alumnoRepository;
    private final ModuloRepository moduloRepository;
    private final MatriculaRepository matriculaRepository;

    // -------------------------------------------------------
    // Operaciones de alumno
    // -------------------------------------------------------

    // Crea un alumno si no existe otro con el mismo NIF
    @Transactional
    public Alumno crearAlumno(Alumno alumno) {
        if (alumno.getNif() == null || alumno.getNombre() == null || alumno.getEmail() == null) {
            throw new IllegalArgumentException("Faltan campos obligatorios del alumno");
        }

        Alumno existente = alumnoRepository.buscarPorNif(alumno.getNif());
        if (existente != null) {
            log.info("El alumno ya existe con id: {}", existente.getId());
            return existente;
        }

        return alumnoRepository.insertar(alumno);
    }

    public Alumno obtenerAlumnoPorId(int id) {
        return alumnoRepository.buscarPorId(id);
    }

    public List<Alumno> obtenerTodosLosAlumnos() {
        return alumnoRepository.buscarTodos();
    }

    @Transactional
    public Alumno actualizarAlumno(Alumno alumno) {
        return alumnoRepository.actualizar(alumno);
    }

    @Transactional
    public void eliminarAlumno(int id) {
        alumnoRepository.eliminar(id);
    }

    // -------------------------------------------------------
    // Operaciones de modulo
    // -------------------------------------------------------

    // Crea un modulo si no existe otro con el mismo codigo
    @Transactional
    public Modulo crearModulo(Modulo modulo) {
        if (modulo.getCodigo() == null || modulo.getNombre() == null || modulo.getHoras() == null) {
            throw new IllegalArgumentException("Faltan campos obligatorios del modulo");
        }

        Modulo existente = moduloRepository.buscarPorCodigo(modulo.getCodigo());
        if (existente != null) {
            log.info("El modulo ya existe con id: {}", existente.getId());
            return existente;
        }

        return moduloRepository.insertar(modulo);
    }

    public Modulo obtenerModuloPorId(int id) {
        return moduloRepository.buscarPorId(id);
    }

    public List<Modulo> obtenerTodosLosModulos() {
        return moduloRepository.buscarTodos();
    }

    @Transactional
    public Modulo actualizarModulo(Modulo modulo) {
        return moduloRepository.actualizar(modulo);
    }

    @Transactional
    public void eliminarModulo(int id) {
        moduloRepository.eliminar(id);
    }

    // -------------------------------------------------------
    // Operaciones de matricula
    // -------------------------------------------------------

    // Matricula un alumno en un modulo comprobando que ambos existen
    @Transactional
    public Matricula matricularAlumno(int idAlumno, int idModulo) {
        Alumno alumno = alumnoRepository.buscarPorId(idAlumno);
        if (alumno == null) {
            throw new IllegalArgumentException("Alumno no encontrado: " + idAlumno);
        }

        Modulo modulo = moduloRepository.buscarPorId(idModulo);
        if (modulo == null) {
            throw new IllegalArgumentException("Modulo no encontrado: " + idModulo);
        }

        if (matriculaRepository.existe(idAlumno, idModulo)) {
            log.info("Ya existe matricula para alumno={} modulo={}", idAlumno, idModulo);
            return new Matricula(idAlumno, idModulo, LocalDate.now());
        }

        Matricula matricula = new Matricula(idAlumno, idModulo, LocalDate.now());
        return matriculaRepository.insertar(matricula);
    }

    public List<Matricula> obtenerTodasLasMatriculas() {
        return matriculaRepository.buscarTodas();
    }

    public List<Matricula> obtenerMatriculasPorAlumno(int idAlumno) {
        return matriculaRepository.buscarPorAlumno(idAlumno);
    }

    @Transactional
    public void eliminarMatricula(int idAlumno, int idModulo) {
        matriculaRepository.eliminar(idAlumno, idModulo);
    }

    // Llama a la funcion almacenada count_enrollments via SimpleJdbcCall
    public int contarMatriculas(int idAlumno) {
        return matriculaRepository.contarMatriculas(idAlumno);
    }
}
