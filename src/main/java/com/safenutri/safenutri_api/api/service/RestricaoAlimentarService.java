package com.safenutri.safenutri_api.api.service;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.input.RestricaoAlimentarInput;
import com.safenutri.safenutri_api.repository.RestricaoAlimentarRepository;

@Service
@RequiredArgsConstructor
public class RestricaoAlimentarService {
    private final RestricaoAlimentarRepository restricaoAlimentarRepository;

    public List<RestricaoAlimentar> findAllById(Set<Long> restricoesIdSet) {
        return restricaoAlimentarRepository.findAllById(restricoesIdSet);
    }

    public List<RestricaoAlimentar> getAllActivated() {
        return restricaoAlimentarRepository.findByActivated(true);
    }

    public void salvar(RestricaoAlimentarInput restricaoAlimentaInput) {
        RestricaoAlimentar restricaoAlimentar = new RestricaoAlimentar(restricaoAlimentaInput.getRestricao(),
            restricaoAlimentaInput.getDescricao());
            
        restricaoAlimentarRepository.save(restricaoAlimentar);
    }
}