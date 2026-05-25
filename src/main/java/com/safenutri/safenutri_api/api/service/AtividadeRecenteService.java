package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.output.AtividadeRecenteOutput;
import com.safenutri.safenutri_api.model.projection.AtividadeRecenteUserProjection;
import com.safenutri.safenutri_api.repository.AtividadeRecenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AtividadeRecenteService {
    private final AtividadeRecenteRepository atividadeRecenteRepositoryRepository;
    private final JwtService jwtService;

    public List<AtividadeRecenteOutput> buscarRecentes() {
        Long userId = jwtService.getIdUsuario();
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
