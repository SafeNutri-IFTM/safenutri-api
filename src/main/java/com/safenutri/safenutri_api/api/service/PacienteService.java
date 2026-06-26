package com.safenutri.safenutri_api.api.service;

import java.util.UUID;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.entity.Paciente;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.repository.NutricionistaRepository;
import com.safenutri.safenutri_api.repository.PacienteRepository;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final NutricionistaRepository nutricionistaRepository;
    private final UserService userService;

    public List<Long> findNutricionistaIdByUserId(Long userId) {
        return pacienteRepository.findAllNutricionistaIdByUserId(userId);
    }

    public Long countPacientes(Long nutricionistaId) {
        return pacienteRepository.countByNutricionistaId(nutricionistaId);
    }

    @Transactional
    public void vincularPorToken(UUID token, Long userId) {
        Nutricionista nutri = nutricionistaRepository.findByTokenConvite(token).orElseThrow(
            () -> new RuntimeException("Link de convite inválido ou expirado.")
        );

        if (nutri.getUser().getId() == userId) {
            throw new IllegalArgumentException("Você não pode se vincular a si mesmo como paciente.");
        }

        boolean jaVinculado = pacienteRepository.existsByUserIdAndNutricionistaId(userId, nutri.getUser().getId());
        if (jaVinculado) {
            throw new IllegalStateException("Você já está vinculado a este nutricionista.");
        }

        User user = userService.findById(userId);
        
        Paciente novoPaciente = new Paciente(user, nutri.getUser());
        pacienteRepository.save(novoPaciente);
    }
}
