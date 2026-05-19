package com.example.aad.service;

import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class CodificacionService {

    public Charset obtenerCodificacion(String opcion) {
        if ("2".equals(opcion)) {
            return StandardCharsets.ISO_8859_1;
        }
        return StandardCharsets.UTF_8;
    }

    public String obtenerNombre(Charset charset) {
        if (StandardCharsets.ISO_8859_1.equals(charset)) {
            return "ISO-8859-1";
        }
        return "UTF-8";
    }
}
