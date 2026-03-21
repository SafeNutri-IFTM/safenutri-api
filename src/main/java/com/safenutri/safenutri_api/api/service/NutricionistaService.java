package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.repository.NutricionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NutricionistaService {
    @Autowired
    private NutricionistaRepository repository;

    public Nutricionista atualizarPerfil(Nutricionista nutricionista) {
        return repository.save(nutricionista);
    }

    public Nutricionista buscarPorId(Long id) {
        return repository.findById(id).orElseThrow();
    }
}