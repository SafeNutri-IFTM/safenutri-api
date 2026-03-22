package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.TipoReceita;
import com.safenutri.safenutri_api.repository.TipoReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoReceitaService {
    @Autowired
    private TipoReceitaRepository repository;

    public List<TipoReceita> listar() {
        return repository.findAll();
    }

    public TipoReceita salvar(TipoReceita tipo) {
        return repository.save(tipo);
    }
}