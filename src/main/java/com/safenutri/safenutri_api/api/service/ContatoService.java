package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.ContatoNutricionista;
import com.safenutri.safenutri_api.repository.ContatoNutricionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContatoService {
    @Autowired
    private ContatoNutricionistaRepository repository;

    public ContatoNutricionista salvar(ContatoNutricionista contato) {
        return repository.save(contato);
    }
}
