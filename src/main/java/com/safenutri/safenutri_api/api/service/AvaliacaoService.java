package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Avaliacao;
import com.safenutri.safenutri_api.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {
    @Autowired
    private AvaliacaoRepository repository;

    public Avaliacao avaliar(Avaliacao avaliacao) {
        // Lógica para limitar estrelas entre 1 e 5 pode ser adicionada aqui
        return repository.save(avaliacao);
    }
}
