package com.safenutri.safenutri_api.api.service;

import java.util.List;

import com.safenutri.safenutri_api.core.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.RestricaoPessoal;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.RestricaoAlimentarInput;
import com.safenutri.safenutri_api.model.output.RestricaoOutput;
import com.safenutri.safenutri_api.repository.RestricaoPessoalRepository;

@Service
@RequiredArgsConstructor
public class RestricaoPessoalService {
    private final RestricaoPessoalRepository restricaoPessoalRepository;
    private final UserService userService;

    public List<RestricaoOutput> getAllActivated(Long userId) {
        return restricaoPessoalRepository.findByActivated(userId);
    }

    public void salvar(RestricaoAlimentarInput restricaoAlimentaInput, Long userId) {
        User user = userService.findById(userId);
        RestricaoPessoal customRestricao = new RestricaoPessoal (user, restricaoAlimentaInput.getRestricao(),
            restricaoAlimentaInput.getDescricao());

        restricaoPessoalRepository.save(customRestricao);
    }
}
