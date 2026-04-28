package com.safenutri.safenutri_api.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.mail.MessagingException;
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
import com.safenutri.safenutri_api.model.output.EmailOutput;
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
    private final EmailSenderService emailSenderService;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

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

        List<Long> restricoesIdList = userInput.getRestricoes();
        if (restricoesIdList != null && !restricoesIdList.isEmpty()) {
            processarRestricoes(savedUser, restricoesIdList);
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

        List<Long> restricoesIdList = userInput.getRestricoes();
        if (restricoesIdList != null && !restricoesIdList.isEmpty()) {
            processarRestricoes(user, restricoesIdList);
        }

        return userRepository.save(user);
    }

    @Transactional
    public void processarRestricoes(User user, List<Long> restricoesIdList) {
        Set<Long> restricoesIdSet = new HashSet<>(restricoesIdList);

        List<RestricaoAlimentar> restricoesList = restricaoAlimentarService.findAllById(restricoesIdSet);

        if (restricoesIdSet.size() != restricoesList.size()) {
            throw new RestricaoNotFoundException("Alguma restrição não existe");
        }

        List<UserRestricao> userRestricoes = new ArrayList<>();

        for (RestricaoAlimentar ra : restricoesList) {
            UserRestricao ur = new UserRestricao(user, ra);
            userRestricoes.add(ur);
        }

        userRestricaoService.deleteByUserId(user.getId());
        userRestricaoService.saveAll(userRestricoes);
    }

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

    public Optional<User> findByExistEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User mudarSenha(User updated, String senha) {
        updated.setSenha(passwordEncoder.encode(senha));
        return userRepository.save(updated);
    }

    public void recuperarSenha(String email) throws MessagingException, IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não cadastrado"));

        if (user.getRole().getId() != 3 && user.getRole().getId() != 2) {
            throw new RuntimeException("Email de usuário não permitido");
        }

        EmailOutput emailOutput = emailSenderService.createEmailRecuperarSenha(user);
        emailSenderService.sendEmail(emailOutput.getEmailUsuario(), emailOutput.getAssunto(),
                emailOutput.getMensagem());
    }
}