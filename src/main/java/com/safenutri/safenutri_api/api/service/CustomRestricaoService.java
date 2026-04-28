package com.safenutri.safenutri_api.api.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.CustomRestricao;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.RestricaoAlimentarInput;
import com.safenutri.safenutri_api.repository.CustomRestricaoRepository;

@Service
@RequiredArgsConstructor
public class CustomRestricaoService {
    private final CustomRestricaoRepository customRestricaoRepository;
    private final UserService userService;
    private final JwtService jwtService;

    public List<CustomRestricao> getAllActivated() {
        User user = userService.findById(jwtService.getIdUsuario());
        return customRestricaoRepository.findByActivated(user.getId());
    }

    public void salvar(RestricaoAlimentarInput restricaoAlimentaInput) {
        User user = userService.findById(jwtService.getIdUsuario());

        Boolean isPublic = false;

        if (jwtService.isNutricionista()) {
            isPublic = true;
        }

        CustomRestricao customRestricao = new CustomRestricao (user, restricaoAlimentaInput.getRestricao(),
            restricaoAlimentaInput.getDescricao(), isPublic);
            
        customRestricaoRepository.save(customRestricao);
    }
}
