package com.Valentin.aad.Service;

import com.Valentin.aad.Model.Student;
import com.Valentin.aad.Repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // añade un alumno al fichero
    public void addStudent(int id, String name, float grade) {
        Student student = new Student(id, name, grade);
        studentRepository.insertStudent(student);
        log.info("Alumno añadido: {}", student);
    }

    // busca un alumno por su posición
    public Optional<Student> getStudentByPosition(int position) {
        Student student = studentRepository.readStudent(position);

        if (student != null) {
            log.info("Alumno encontrado en {}: {}", position, student);
            return Optional.of(student);
        }

        log.warn("No hay alumno en la posición {}", position);
        return Optional.empty();
    }

    // cambia la nota de un alumno sin tocar el resto
    public void updateStudentGrade(int position, float newGrade) {
        studentRepository.updateGrade(position, newGrade);
        log.info("Nota actualizada en {}: {}", position, newGrade);
    }
}
