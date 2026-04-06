package com.safenutri.safenutri_api.api.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import necessário
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.repository.RestricaoAlimentarRepository;

@Service
@RequiredArgsConstructor
public class RestricaoAlimentarService {
    private final RestricaoAlimentarRepository restricaoAlimentarRepository;

    public List<RestricaoAlimentar> findAllById(Set<Long> restricoesIdSet) {
        return restricaoAlimentarRepository.findAllById(restricoesIdSet);
    }

    public List<RestricaoAlimentar> getAllActivatedAndApproved() {
        return restricaoAlimentarRepository.findByActivatedAndApproved(true, true);
    }

    @Transactional
    public RestricaoAlimentar salvar(RestricaoAlimentar restricao) {
        // O repositório salvará o objeto na tabela 'restricao_alimentar'
        return restricaoAlimentarRepository.save(restricao);
    }
}