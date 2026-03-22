package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.IngredienteReceita;
import com.safenutri.safenutri_api.repository.IngredienteReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IngredienteReceitaService {
    @Autowired
    private IngredienteReceitaRepository repository;

    public IngredienteReceita adicionar(IngredienteReceita item) {
        return repository.save(item);
    }

    public List<IngredienteReceita> buscarPorReceita(Long id) {
        return repository.findByReceitaId(id);
    }
}
