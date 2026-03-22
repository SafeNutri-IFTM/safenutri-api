package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Paciente;
import com.safenutri.safenutri_api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository repository;

    public Paciente vincularNutricionista(Paciente paciente) {
        return repository.save(paciente);
    }
}
