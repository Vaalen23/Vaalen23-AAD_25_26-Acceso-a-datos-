package com.valentin.aad.repository;

import com.valentin.aad.model.Enrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByModuleId(Long moduleId);

    int countByStudentId(Long studentId);

    @EntityGraph(attributePaths = {"student", "module"})
    @Query("SELECT e FROM Enrollment e")
    List<Enrollment> findAllWithStudentAndModule();

    @Query("SELECT e FROM Enrollment e WHERE e.finalGrade >= :minGrade ORDER BY e.finalGrade DESC")
    List<Enrollment> findByMinFinalGrade(@Param("minGrade") Double minGrade);

    @Query("SELECT e FROM Enrollment e WHERE LOWER(e.student.name) = LOWER(:name) AND e.finalGrade >= :minGrade ORDER BY e.finalGrade DESC")
    List<Enrollment> findByStudentNameAndMinGrade(@Param("name") String name, @Param("minGrade") Double minGrade);

    @Query("SELECT COALESCE(AVG(e.finalGrade), 0) FROM Enrollment e WHERE e.module.id = :moduleId AND e.finalGrade IS NOT NULL")
    Double calculateAverageGradeByModule(@Param("moduleId") Long moduleId);
}
