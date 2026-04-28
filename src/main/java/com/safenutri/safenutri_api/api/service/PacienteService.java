package com.safenutri.safenutri_api.api.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.repository.PacienteRepository;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public List<Long> findNutricionistaIdByUserId(Long userId) {
        return pacienteRepository.findAllNutricionistaIdByUserId(userId);
    }

    public Long countPacientes(Long nutricionistaId) {
        return pacienteRepository.countByNutricionistaId(nutricionistaId);
    }
}
