package com.safenutri.safenutri_api.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.exception.EmailAlreadyExistsException;
import com.safenutri.safenutri_api.exception.RestricaoNotFoundException;
import com.safenutri.safenutri_api.exception.SenhaInvalidaException;
import com.safenutri.safenutri_api.model.entity.AceitoTermos;
import com.safenutri.safenutri_api.model.entity.DocumentosLegais;
import com.safenutri.safenutri_api.model.entity.Genero;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.entity.Role;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.entity.UserRestricao;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.repository.UserRepository;
import com.safenutri.safenutri_api.repository.AceitoTermosRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final RoleService roleService;
    private final GeneroService generoService;
    private final RestricaoAlimentarService restricaoAlimentarService;
    private final UserRestricaoService userRestricaoService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentosLegaisService documentosService;
    private final AceitoTermosRepository aceitoTermosRepository;

    @Transactional
    public User save(UserInput userInput) {
        if (userRepository.existsByEmail(userInput.getEmail())) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        Role role = roleService.findById(userInput.getRole());
        Genero genero = generoService.findById(userInput.getGenero());

        User user = new User(role, genero, userInput.getNome(), userInput.getEmail(),
                passwordEncoder.encode(userInput.getSenha()), userInput.getDtNascimento());

        User savedUser = userRepository.save(user);

        DocumentosLegais termoComum = documentosService.buscarAtualPorTipo("TERMO_USO_COMUM");
        DocumentosLegais politicaPriv = documentosService.buscarAtualPorTipo("POLITICA_PRIVACIDADE");

        aceitoTermosRepository.save(new AceitoTermos(savedUser, termoComum));
        aceitoTermosRepository.save(new AceitoTermos(savedUser, politicaPriv));

        if (userInput.getRestricoes() != null && !userInput.getRestricoes().isEmpty()) {
            processarRestricoes(savedUser, userInput);
        }
        return savedUser;
    }

    @Transactional
    public User atualizarPerfil(UserInput userInput) {
        User user = userRepository.findById(userInput.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        user.setNome(userInput.getNome());
        user.setDtNascimento(userInput.getDtNascimento());

        if (userInput.getGenero() != null) {
            Genero genero = generoService.findById(userInput.getGenero());
            user.setGenero(genero);
        }

        if (userInput.getSenha() != null && !userInput.getSenha().trim().isEmpty()) {
            user.setSenha(passwordEncoder.encode(userInput.getSenha()));
        }

        if (userInput.getRestricoes() != null) {

            userRestricaoService.deleteByUserId(user.getId());

            if (!userInput.getRestricoes().isEmpty()) {
                processarRestricoes(user, userInput);
            }
        }

        return userRepository.save(user);
    }

    private void processarRestricoes(User user, UserInput userInput) {
        List<Long> restricoesId = userInput.getRestricoes();
        Set<Long> restricoesIdSet = new HashSet<>(restricoesId);

        List<RestricaoAlimentar> restricoes = restricaoAlimentarService.findAllById(restricoesIdSet);

        if (restricoesId.size() != restricoes.size()) {
            throw new RestricaoNotFoundException("Alguma restrição não existe");
        }

        List<UserRestricao> userRestricoes = new ArrayList<>();

        for (RestricaoAlimentar ra : restricoes) {
            UserRestricao ur = new UserRestricao(user, ra);
            userRestricoes.add(ur);
        }

        userRestricaoService.saveAll(userRestricoes);
    }

    @Transactional // <--- A correção exata e funcional está aqui!
    public User autenticarPorEmail(String email, String password)
            throws UsernameNotFoundException, SenhaInvalidaException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        if (!passwordEncoder.matches(password, user.getSenha())) {
            throw new SenhaInvalidaException();
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!user.getActivated()) {
            throw new UsernameNotFoundException("Usuário desativado.");
        }

        String roleNome = user.getRole().getRole();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getSenha())
                .roles(roleNome)
                .build();
    }

    public User getReference(Long id) {
        return userRepository.getReferenceById(id);
    }

    public User getProfileByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
    }
}