package com.valentin.aad.service;

import com.valentin.aad.exception.ModuleNotFoundException;
import com.valentin.aad.model.Module;
import com.valentin.aad.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;

    @Transactional
    public Module create(Module module) {
        return moduleRepository.save(module);
    }

    @Transactional(readOnly = true)
    public List<Module> findAll() {
        return moduleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Module findById(Long id) {
        return moduleRepository.findById(id).orElseThrow(() -> new ModuleNotFoundException(id));
    }

    @Transactional
    public void delete(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new ModuleNotFoundException(id);
        }
        moduleRepository.deleteById(id);
    }
}
