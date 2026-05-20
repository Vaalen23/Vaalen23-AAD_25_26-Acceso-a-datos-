package com.valentin.aad.service;

import com.valentin.aad.exception.StudentNotFoundException;
import com.valentin.aad.model.Student;
import com.valentin.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Student> findAllWithRelations() {
        return studentRepository.findAllWithProfileAndEnrollments();
    }

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Student findByNif(String nif) {
        return studentRepository.findByNif(nif).orElseThrow(() -> new StudentNotFoundException(nif));
    }

    @Transactional(readOnly = true)
    public List<Student> searchByEmail(String text) {
        return studentRepository.searchByEmail(text);
    }

    @Transactional
    public Student updateEmail(Long id, String email) {
        Student student = findById(id);
        student.setEmail(email);
        return student;
    }

    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }
}
