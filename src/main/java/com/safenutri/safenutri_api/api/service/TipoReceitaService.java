package com.safenutri.safenutri_api.api.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.TipoReceita;
import com.safenutri.safenutri_api.repository.TipoReceitaRepository;

@Service
@RequiredArgsConstructor
public class TipoReceitaService {
    private final TipoReceitaRepository tipoReceitaRepository;

    public TipoReceita findById(Long id) {
        return tipoReceitaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tipo de Receita não encontrada"));
    }

    public TipoReceita getReference(Long id) {
        return tipoReceitaRepository.getReferenceById(id);
    }

    public List<TipoReceita> getAll() {
        return tipoReceitaRepository.findAll();
    }
}
