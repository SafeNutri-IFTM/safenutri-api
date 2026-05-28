package com.safenutri.safenutri_api.api.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.output.AtividadeRecenteOutput;
import com.safenutri.safenutri_api.model.projection.AtividadeRecenteUserProjection;
import com.safenutri.safenutri_api.repository.AtividadeRecenteRepository;

@Service
@RequiredArgsConstructor
public class AtividadeRecenteService {
    private final AtividadeRecenteRepository atividadeRecenteRepositoryRepository;

    public List<AtividadeRecenteOutput> buscarRecentes(Long userId) {
        List<AtividadeRecenteUserProjection> resultado = atividadeRecenteRepositoryRepository.buscarAtividadesRecentes(userId);

        return resultado.stream().map(p -> new AtividadeRecenteOutput(
                p.getTipoAtividade(),
                p.getReceitaId(),
                p.getReceitaTitulo(),
                p.getEstrela(),
                p.getDataAtividade()
        )).toList();
    }
}
