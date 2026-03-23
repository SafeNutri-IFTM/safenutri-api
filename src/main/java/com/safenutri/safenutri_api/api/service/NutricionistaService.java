package com.safenutri.safenutri_api.api.service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.NutricionistaInput;
import com.safenutri.safenutri_api.repository.NutricionistaRepository;

@Service
@RequiredArgsConstructor
public class NutricionistaService {

    private final NutricionistaRepository nutricionistaRepository;
    private final UserService userService;

    @Transactional
    public void save(NutricionistaInput nutricionistaInput) {
        User savedUser = userService.save(nutricionistaInput.getUserInput());

        // validação do crn do nutricionista
        // if () {
        // }

        Nutricionista nutricionista = new Nutricionista(savedUser, nutricionistaInput.getCRN());

        nutricionistaRepository.save(nutricionista);
    }
}
