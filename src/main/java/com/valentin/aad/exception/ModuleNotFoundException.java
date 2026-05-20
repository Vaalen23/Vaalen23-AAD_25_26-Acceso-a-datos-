package com.valentin.aad.exception;

public class ModuleNotFoundException extends RuntimeException {

    public ModuleNotFoundException(Long id) {
        super("Module not found with id: " + id);
    }
}
