package com.valentin.aad.service;

import com.valentin.aad.exception.EnrollmentNotFoundException;
import com.valentin.aad.exception.ModuleNotFoundException;
import com.valentin.aad.exception.StudentNotFoundException;
import com.valentin.aad.model.Enrollment;
import com.valentin.aad.model.Module;
import com.valentin.aad.model.Student;
import com.valentin.aad.repository.EnrollmentRepository;
import com.valentin.aad.repository.ModuleRepository;
import com.valentin.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public Enrollment enrollStudent(Long studentId, Long moduleId) {
        return enrollStudent(studentId, moduleId, null);
    }

    @Transactional
    public Enrollment enrollStudent(Long studentId, Long moduleId, Double finalGrade) {
        if (finalGrade != null && (finalGrade < 0 || finalGrade > 10)) {
            throw new IllegalArgumentException("Final grade must be between 0 and 10");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        Enrollment enrollment = new Enrollment(student, module, LocalDate.now(), finalGrade);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment enrollStudentWithError(Long studentId, Long moduleId) {
        Enrollment enrollment = enrollStudent(studentId, moduleId, 5.0);
        throw new RuntimeException("Simulated error");
    }

    @Transactional(readOnly = true)
    public List<Enrollment> listAllEnrollments() {
        return enrollmentRepository.findAllWithStudentAndModule();
    }

    @Transactional(readOnly = true)
    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id).orElseThrow(() -> new EnrollmentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Enrollment> findByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public int countEnrollments(Long studentId) {
        return enrollmentRepository.countByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> findHighGrades(Double minGrade) {
        return enrollmentRepository.findByMinFinalGrade(minGrade);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> findByStudentNameAndMinGrade(String name, Double minGrade) {
        return enrollmentRepository.findByStudentNameAndMinGrade(name, minGrade);
    }

    @Transactional(readOnly = true)
    public Double averageGradeByModule(Long moduleId) {
        return enrollmentRepository.calculateAverageGradeByModule(moduleId);
    }
}
