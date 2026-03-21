package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Ingrediente;
import com.safenutri.safenutri_api.repository.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IngredienteService {
    @Autowired
    private IngredienteRepository repository;

    public Ingrediente salvar(Ingrediente ingrediente) {
        return repository.save(ingrediente);
    }

    public List<Ingrediente> listar() {
        return repository.findAll();
    }
}
