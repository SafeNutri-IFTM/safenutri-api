package com.safenutri.safenutri_api.api.service;

import java.util.List;
import java.util.Set;

import com.safenutri.safenutri_api.model.output.RestricaoListOutput;
import com.safenutri.safenutri_api.model.output.RestricaoOutput;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.input.RestricaoAlimentarInput;
import com.safenutri.safenutri_api.repository.RestricaoAlimentarRepository;

@Service
@RequiredArgsConstructor
public class RestricaoAlimentarService {
    private final RestricaoAlimentarRepository restricaoAlimentarRepository;

    @Transactional(readOnly = true)
    public List<RestricaoAlimentar> findAllById(Set<Long> restricoesIdSet) {
        return restricaoAlimentarRepository.findAllById(restricoesIdSet);
    }

    @Transactional(readOnly = true)
    public List<RestricaoOutput> getAllActivated() {
        return restricaoAlimentarRepository.findByActivated();
    }

    @Transactional(readOnly = true)
    public Page<RestricaoListOutput> getAllActivatedComDesc(int page, int size, boolean ativo) {
        return restricaoAlimentarRepository.findByActivatedComDesc(PageRequest.of(page, size), ativo);
    }

    @Transactional
    public void salvar(RestricaoAlimentarInput restricaoAlimentaInput) {
        RestricaoAlimentar restricaoAlimentar = new RestricaoAlimentar(restricaoAlimentaInput.getRestricao(),
            restricaoAlimentaInput.getDescricao());
            
        restricaoAlimentarRepository.save(restricaoAlimentar);
    }

    public Long countAll() {
        return restricaoAlimentarRepository.countAll();
    }
}