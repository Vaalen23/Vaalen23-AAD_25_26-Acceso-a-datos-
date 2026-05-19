package com.example.aad.service;

import java.util.List;

public interface ConversorService<T> {

    void exportar(List<T> elementos) throws Exception;
}
