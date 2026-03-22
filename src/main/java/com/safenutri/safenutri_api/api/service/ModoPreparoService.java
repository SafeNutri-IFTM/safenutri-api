package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.ModoPreparo;
import com.safenutri.safenutri_api.repository.ModoPreparoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModoPreparoService {
    @Autowired
    private ModoPreparoRepository repository;

    public List<ModoPreparo> listarPassos(Long receitaId) {
        return repository.findByReceitaIdOrderByPassoAsc(receitaId);
    }
}