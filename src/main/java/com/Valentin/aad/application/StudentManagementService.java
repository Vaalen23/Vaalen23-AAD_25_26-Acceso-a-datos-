package com.Valentin.aad.application;

import com.Valentin.aad.model.Module;
import com.Valentin.aad.model.Student;
import com.Valentin.aad.repository.EnrollmentStatsRepository;
import com.Valentin.aad.repository.ModuleRepository;
import com.Valentin.aad.repository.StudentRepository;
import com.Valentin.aad.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentManagementService {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;
    private final EnrollmentService enrollmentService;
    private final EnrollmentStatsRepository enrollmentStatsRepository;

    public Student createStudent(Student student) {
        return studentRepository.insert(student);
    }

    public Module createModule(Module module) {
        return moduleRepository.insert(module);
    }

    public int countEnrollments(Integer studentId) {
        return enrollmentStatsRepository.countEnrollments(studentId);
    }

    public void enrollStudentInModule(Integer studentId, Integer moduleId) {
        enrollmentService.enrollStudentInModule(studentId, moduleId);
    }
}