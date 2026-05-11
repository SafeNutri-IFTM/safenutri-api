package com.safenutri.safenutri_api.api.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ReceitaService receitaService;
    private final PacienteService pacienteService;

    public Page<ReceitaCardOutput> getReceitas(Long userId, int page, int size) {
        return receitaService.findReceitasByRestricoesId(userId, page, size);
    }

    public Page<ReceitaCardOutput> buscarReceitasPaginadas(String termo, int page, int size) {
        return receitaService.buscarReceitasPaginadas(termo, page, size);
    }

    public List<ReceitaCardOutput> getTop3ReceitasFiltradas(Long userId) {
        return receitaService.findTop3Filtrado(userId);
    }

    public Long countPacientes(Long nutricionistaId) {
        return pacienteService.countPacientes(nutricionistaId);
    }

    public Long countReceitas(Long nutricionistaId) {
        return receitaService.countByNutricionistaId(nutricionistaId);
    }

    public List<ReceitaPieChartOutput> countGroupByRestricoes(Long nutricionistaId) {
        return receitaService.countGroupByRestricoes(nutricionistaId);
    }

    public List<ReceitaCardOutput> getTop3GeralReceitas() {
        return receitaService.findTop3();
    }
}