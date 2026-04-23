package com.safenutri.safenutri_api.api.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.ModoPreparo;
import com.safenutri.safenutri_api.repository.ModoPreparoRepository;

@Service
@RequiredArgsConstructor
public class ModoPreparoService {
    private final ModoPreparoRepository modoPreparoRepository;

    public void saveAll(List<ModoPreparo> modosPreparo) {
        modoPreparoRepository.saveAll(modosPreparo);
    }

    public void deleteByReceitaId(Long id) {
        modoPreparoRepository.deleteByReceitaId(id);
    }

}
