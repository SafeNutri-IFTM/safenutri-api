package com.safenutri.safenutri_api.api.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ReceitaService receitaService;
    private final UserRestricaoService userRestricaoService;
    private final UserService userService;

    public Page<ReceitaDashboardOutput> getReceitas(int page, int size, Long userId) {
        User user = userService.findById(userId);
        List<Long> restricoesIds = userRestricaoService.findAllByUserId(userId);
        Long qntd = (long) restricoesIds.size();
        Pageable pageable = PageRequest.of(page, size);

        return receitaService.findReceitasByRestricoesId(user, restricoesIds, qntd, pageable);
    }

    public List<ReceitaDashboardOutput> getTop3Receitas(Long userId) {
        List<Long> restricoesIds = userRestricaoService.findAllByUserId(userId);
        Long qntd = (long) restricoesIds.size();

        return receitaService.findTop3(restricoesIds, qntd);
    }
}
