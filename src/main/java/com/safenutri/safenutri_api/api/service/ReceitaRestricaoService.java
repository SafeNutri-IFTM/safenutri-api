package com.safenutri.safenutri_api.api.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.ReceitaRestricao;
import com.safenutri.safenutri_api.model.output.RestricaoOutput;
import com.safenutri.safenutri_api.repository.ReceitaRestricaoRepository;

@Service
@RequiredArgsConstructor
public class ReceitaRestricaoService {
    private final ReceitaRestricaoRepository receitaRestricaoRepository;

    public void saveAll(List<ReceitaRestricao> receitaRestricoes) {
        receitaRestricaoRepository.saveAll(receitaRestricoes);
    }

    public void deleteByReceitaId(Long id) {
        receitaRestricaoRepository.deleteByReceitaId(id);
    }

    public List<RestricaoOutput> findByReceitaId(Long id) {
        return receitaRestricaoRepository.findByReceitaId(id);
    }
}
