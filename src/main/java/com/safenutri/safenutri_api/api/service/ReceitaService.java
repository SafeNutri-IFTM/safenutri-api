package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository repository;

    public Receita salvar(Receita receita) {
        // Futuramente: validar se o autor (nutricionista) existe
        return repository.save(receita);
    }

    public List<Receita> listarTodas() {
        return repository.findAll();
    }
}
