package com.safenutri.safenutri_api.api.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.DocumentosLegais;
import com.safenutri.safenutri_api.repository.DocumentosLegaisRepository;

@Service
@RequiredArgsConstructor
public class DocumentosLegaisService {
    private final DocumentosLegaisRepository repository;

    public DocumentosLegais buscarAtualPorTipo(String tipo) {
        return repository.findFirstByTipoAndActivatedTrueOrderByCreatedDesc(tipo)
                .orElseThrow(() -> new RuntimeException("Documento legal não configurado: " + tipo));
    }
}
