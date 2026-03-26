package com.safenutri.safenutri_api.api.service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.core.utils.CrnValidation;
import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.NutricionistaInput;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.repository.NutricionistaRepository;

@Service
@RequiredArgsConstructor
public class NutricionistaService {

    private final NutricionistaRepository nutricionistaRepository;
    private final UserService userService;
    private final CrnValidation crnValidation;

    @Transactional
    public void save(NutricionistaInput nutricionistaInput) {
        UserInput userInput = nutricionistaInput.getUserInput();
        User savedUser = userService.save(userInput);
        
        Boolean crnValido = crnValidation.pesquisarCrn(userInput.getNome(), 
            nutricionistaInput.getCRN(), nutricionistaInput.getInscricao());

        if (crnValido == null) {
            throw new RuntimeException("Erro ao validar CRN");
        } 
        
        if (Boolean.FALSE.equals(crnValido)) {
            throw new RuntimeException("CRN inválido");
        }

        Nutricionista nutricionista = new Nutricionista(savedUser, nutricionistaInput.getCRN());
        nutricionistaRepository.save(nutricionista);
    }
}
