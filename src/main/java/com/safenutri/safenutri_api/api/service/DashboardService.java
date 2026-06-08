package com.safenutri.safenutri_api.api.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.safenutri.safenutri_api.model.output.QtdUserByMesOutput;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;
import com.safenutri.safenutri_api.model.output.UserPieChartOutput;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ReceitaService receitaService;
    private final PacienteService pacienteService;
    private final RestricaoAlimentarService restricaoAlimentarService;
    private final UserService userService;

    public Page<ReceitaCardOutput> getReceitas(Long userId, int page, int size) {
        return receitaService.findReceitasByRestricoesId(userId, page, size);
    }

    public List<ReceitaCardOutput> getTop3ReceitasFiltradas(Long userId) {
        return receitaService.getTop3Filtrado(userId);
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
        return receitaService.getTop3();
    }

    public Long countAllReceitas() {
        return receitaService.countAll();
    }

    public Long countAllRestricoes() {
        return restricaoAlimentarService.countAll();
    }

    public Long countAllUsers() {
        return userService.countAll();
    }

    public List<UserPieChartOutput> countUserGroupByRestricao() {
        return userService.countUserGroupByRestricao();
    }

    public List<QtdUserByMesOutput> countUserByMes() {
        return userService.getQuantidadeUsuariosByMes();
    }
}
