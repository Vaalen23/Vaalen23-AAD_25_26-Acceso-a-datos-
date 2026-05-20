package com.valentin.aad.service;

import com.valentin.aad.model.Enrollment;
import com.valentin.aad.model.Module;
import com.valentin.aad.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentManagementService {

    private final StudentService studentService;
    private final ModuleService moduleService;
    private final EnrollmentService enrollmentService;

    @Transactional
    public Student createStudent(Student student) {
        return studentService.create(student);
    }

    @Transactional
    public Module createModule(Module module) {
        return moduleService.create(module);
    }

    @Transactional
    public Enrollment enrollStudentInModule(Long studentId, Long moduleId) {
        return enrollmentService.enrollStudent(studentId, moduleId);
    }

    @Transactional
    public Enrollment enrollStudentInModule(Long studentId, Long moduleId, Double finalGrade) {
        return enrollmentService.enrollStudent(studentId, moduleId, finalGrade);
    }

    @Transactional(readOnly = true)
    public int countEnrollments(Long studentId) {
        return enrollmentService.countEnrollments(studentId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> listAllEnrollments() {
        return enrollmentService.listAllEnrollments();
    }

    @Transactional(readOnly = true)
    public List<Student> searchStudentsByEmail(String text) {
        return studentService.searchByEmail(text);
    }
}
