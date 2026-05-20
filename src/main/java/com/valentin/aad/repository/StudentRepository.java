package com.valentin.aad.repository;

import com.valentin.aad.model.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByNif(String nif);

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByEmailContainingIgnoreCase(String email);

    @Query("SELECT s FROM Student s WHERE LOWER(s.email) LIKE LOWER(CONCAT('%', :text, '%'))")
    List<Student> searchByEmail(@Param("text") String text);

    @EntityGraph(attributePaths = {"profile", "enrollments"})
    @Query("SELECT s FROM Student s")
    List<Student> findAllWithProfileAndEnrollments();
}
