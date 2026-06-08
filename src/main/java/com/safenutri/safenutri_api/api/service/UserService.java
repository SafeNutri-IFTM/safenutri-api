package com.safenutri.safenutri_api.api.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safenutri.safenutri_api.core.security.UserPrincipal;
import com.safenutri.safenutri_api.exception.UserNotFoundException;
import com.safenutri.safenutri_api.exception.EmailAlreadyExistsException;
import com.safenutri.safenutri_api.exception.RestricaoNotFoundException;
import com.safenutri.safenutri_api.exception.SenhaInvalidaException;
import com.safenutri.safenutri_api.model.entity.AceitoTermos;
import com.safenutri.safenutri_api.model.entity.DocumentosLegais;
import com.safenutri.safenutri_api.model.entity.Genero;
import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.entity.Role;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.entity.UserRestricao;
import com.safenutri.safenutri_api.model.enums.RoleEnum;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.model.output.EmailOutput;
import com.safenutri.safenutri_api.model.output.QtdUserByMesOutput;
import com.safenutri.safenutri_api.model.output.UserOutput;
import com.safenutri.safenutri_api.model.output.UserPieChartOutput;
import com.safenutri.safenutri_api.repository.UserRepository;
import com.safenutri.safenutri_api.repository.AceitoTermosRepository;
import com.safenutri.safenutri_api.repository.NutricionistaRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final RoleService roleService;
    private final GeneroService generoService;
    private final RestricaoAlimentarService restricaoAlimentarService;
    private final UserRestricaoService userRestricaoService;
    private final UserRepository userRepository;
    private final NutricionistaRepository nutricionistaRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentosLegaisService documentosService;
    private final AceitoTermosRepository aceitoTermosRepository;
    private final EmailSenderService emailSenderService;

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
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

        DocumentosLegais termoComum = documentosService.buscarEntidadeAtualPorTipo("TERMO_USO_COMUM");
        DocumentosLegais politicaPriv = documentosService.buscarEntidadeAtualPorTipo("POLITICA_PRIVACIDADE");

        aceitoTermosRepository.save(new AceitoTermos(savedUser, termoComum));
        aceitoTermosRepository.save(new AceitoTermos(savedUser, politicaPriv));

        List<Long> restricoesIdList = userInput.getRestricoes();
        if (restricoesIdList != null && !restricoesIdList.isEmpty()) {
            processarRestricoes(savedUser, restricoesIdList);
        }
        return savedUser;
    }

    @Transactional
    public User atualizarPerfil(Long userId, UserInput userInput) {
        User user = userRepository.findById(userId)
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

        if ("NUTRICIONISTA".equals(user.getRole().getRole()) && userInput.getSobreMim() != null) {
            Optional<Nutricionista> nutricionista = nutricionistaRepository
                    .findByUserId(user.getId());
            if (nutricionista.isPresent()) {
                nutricionista.get().setDescPerfil(userInput.getSobreMim());
                nutricionistaRepository.save(nutricionista.get());
            }
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

        if ("GOOGLE".equals(user.getLoginProvider()) && user.getSenha() == null) {
            throw new RuntimeException(
                    "Este e-mail está vinculado ao login do Google. Por favor, clique em 'Entrar com o Google'.");
        }

        if (user.getSenha() == null ||
                !passwordEncoder.matches(password, user.getSenha())) {

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

        return new UserPrincipal(user);
    }

    public User getReference(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public UserOutput getProfileInfos(Long userId) {
        UserOutput userOutput = userRepository.getProfileInfos(userId);
        Long diasConta = ChronoUnit.DAYS.between(userOutput.getCreated().toLocalDate(), LocalDate.now());
        userOutput.setDiasConta(diasConta);

        return userOutput;
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

        if ("GOOGLE".equals(user.getLoginProvider())) {
            throw new RuntimeException(
                    "Esta conta utiliza o login do Google. Não é possível alterar a senha por aqui.");
        }

        if (user.getRole().getId() != 3 && user.getRole().getId() != 2) {
            throw new RuntimeException("Email de usuário não permitido");
        }

        EmailOutput emailOutput = emailSenderService.createEmailRecuperarSenha(user);
        emailSenderService.sendEmail(emailOutput.getEmailUsuario(), emailOutput.getAssunto(),
                emailOutput.getMensagem());
    }

    @Transactional
    public User loginGoogle(String email, String nome) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    if ("LOCAL".equals(user.getLoginProvider())) {
                        throw new RuntimeException("Este email já está cadastrado com login e senha.");
                    }

                    boolean alterado = false;
                    if (user.getNome() == null || user.getNome().isBlank()) {
                        user.setNome(nome);
                        alterado = true;
                    }

                    if (alterado) {
                        return userRepository.save(user);
                    }
                    return user;
                })
                .orElseThrow(() -> new RuntimeException("USUARIO_NAO_ENCONTRADO_GOOGLE"));
    }

    @Transactional
    public User cadastrarUsuarioComumGoogle(String email, String nome, List<Long> restricoesIds) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        Role role = roleService.findById(RoleEnum.USER.getId());

        Genero genero = null;
        LocalDate dataNascimento = null;

        User user = new User(role, genero, nome, email, null, dataNascimento);

        user.setActivated(true);
        user.setLoginProvider("GOOGLE");

        User savedUser = saveGoogleUser(user);

        DocumentosLegais termoComum = documentosService.buscarEntidadeAtualPorTipo("TERMO_USO_COMUM");
        DocumentosLegais politicaPriv = documentosService.buscarEntidadeAtualPorTipo("POLITICA_PRIVACIDADE");

        aceitoTermosRepository.save(new AceitoTermos(savedUser, termoComum));
        aceitoTermosRepository.save(new AceitoTermos(savedUser, politicaPriv));

        if (restricoesIds != null && !restricoesIds.isEmpty()) {
            processarRestricoes(savedUser, restricoesIds);
        }

        return savedUser;
    }

    @Transactional
    public User saveGoogleUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deactivateAccount(Long userId) {
        User user = findById(userId);
        user.setActivated(false);
    }

    @Transactional
    public void activateAccount(Long userId) {
        User user = findById(userId);
        user.setActivated(true);
    }

    public Long countAll() {
        return userRepository.countAll();
    }

    public List<UserPieChartOutput> countUserGroupByRestricao(){
        return userRepository.countUserGroupByRestricao();
    }

    public List<QtdUserByMesOutput> getQuantidadeUsuariosByMes() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

        return userRepository.getQuantidadeUsuariosByMes()
                .stream()
                .map(p -> new QtdUserByMesOutput(
                        p.getMes().format(formatter),
                        p.getQntd()
                ))
                .toList();
    }

}