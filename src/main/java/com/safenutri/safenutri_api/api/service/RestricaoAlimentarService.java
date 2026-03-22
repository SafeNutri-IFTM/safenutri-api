package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.repository.RestricaoAlimentarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RestricaoAlimentarService {
    @Autowired
    private RestricaoAlimentarRepository repository;

    public List<RestricaoAlimentar> listarTodas() {
        return repository.findAll();
    }

    public RestricaoAlimentar salvar(RestricaoAlimentar restricao) {
        return repository.save(restricao);
    }
}