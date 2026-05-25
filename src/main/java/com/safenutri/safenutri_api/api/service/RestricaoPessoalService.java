package com.safenutri.safenutri_api.api.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.RestricaoPessoal;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.RestricaoAlimentarInput;
import com.safenutri.safenutri_api.repository.RestricaoPessoalRepository;

@Service
@RequiredArgsConstructor
public class RestricaoPessoalService {
    private final RestricaoPessoalRepository restricaoPessoalRepository;
    private final UserService userService;
    private final JwtService jwtService;

    public List<RestricaoPessoal> getAllActivated() {
        User user = userService.findById(jwtService.getIdUsuario());
        return restricaoPessoalRepository.findByActivated(user.getId());
    }

    public void salvar(RestricaoAlimentarInput restricaoAlimentaInput) {
        User user = userService.findById(jwtService.getIdUsuario());

        RestricaoPessoal customRestricao = new RestricaoPessoal (user, restricaoAlimentaInput.getRestricao(),
            restricaoAlimentaInput.getDescricao());

        restricaoPessoalRepository.save(customRestricao);
    }
}
