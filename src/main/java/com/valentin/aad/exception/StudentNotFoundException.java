package com.valentin.aad.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }

    public StudentNotFoundException(String nif) {
        super("Student not found with nif: " + nif);
    }
}
