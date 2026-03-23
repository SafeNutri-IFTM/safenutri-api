package com.safenutri.safenutri_api.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.exception.EmailAlreadyExistsException;
import com.safenutri.safenutri_api.exception.RestricaoNotFoundException;
import com.safenutri.safenutri_api.model.entity.Genero;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.entity.Role;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.entity.UserRestricao;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final RoleService roleService;
    private final GeneroService generoService;
    private final RestricaoAlimentarService restricaoAlimentarService;
    private final UserRestricaoService userRestricaoService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(UserInput userInput) {
        if(userRepository.existsByEmail(userInput.getEmail())) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        Role role = roleService.findById(3L);
        Genero genero = generoService.findById(userInput.getGenero());

        User user = new User(role, genero, userInput.getNome(), userInput.getEmail(), 
            passwordEncoder.encode(userInput.getSenha()), userInput.getDtNascimento()
        );

        User savedUser = userRepository.save(user);
        if (userInput.getRestricoes() != null && !userInput.getRestricoes().isEmpty()) {
            processarRestricoes(savedUser, userInput);
        }
        return savedUser;
    }

    private void processarRestricoes(User user, UserInput userInput) {
        List<Long> restricoesId = userInput.getRestricoes();
        Set<Long> restricoesIdSet = new HashSet<>(restricoesId);

        List<RestricaoAlimentar> restricoes = restricaoAlimentarService.findAllById(restricoesIdSet);

        if (restricoesId.size() != restricoes.size()) {
            throw new RestricaoNotFoundException("Alguma restrição não existe"); 
        }

        List<UserRestricao> lista = new ArrayList<>();

        for (RestricaoAlimentar ra: restricoes) {
            UserRestricao ur = new UserRestricao(user, ra);
            lista.add(ur);
        }

        userRestricaoService.saveAll(lista);
    }
}