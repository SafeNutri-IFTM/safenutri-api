package com.safenutri.safenutri_api.api.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.safenutri.safenutri_api.core.utils.CrnValidation;

import com.safenutri.safenutri_api.exception.UserNotFoundException;

import com.safenutri.safenutri_api.model.entity.AceitoTermos;
import com.safenutri.safenutri_api.model.entity.DocumentosLegais;
import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.entity.Role;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.enums.RoleEnum;
import com.safenutri.safenutri_api.model.input.NutricionistaInput;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.model.output.*;

import com.safenutri.safenutri_api.repository.AceitoTermosRepository;
import com.safenutri.safenutri_api.repository.NutricionistaRepository;
import com.safenutri.safenutri_api.repository.PacienteRepository;
import com.safenutri.safenutri_api.repository.ReceitaAvaliacaoRepository;
import com.safenutri.safenutri_api.repository.ReceitaFavoritaRepository;
import com.safenutri.safenutri_api.repository.ReceitaRepository;

@Service
@RequiredArgsConstructor
public class NutricionistaService {
    private final NutricionistaRepository nutricionistaRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final CrnValidation crnValidation;
    private final DocumentosLegaisService documentosService;
    private final AceitoTermosRepository aceitoTermosRepository;
    private final PacienteRepository pacienteRepository;
    private final ReceitaAvaliacaoRepository receitaAvaliacaoRepository;
    private final ReceitaFavoritaRepository receitaFavoritaRepository;
    private final ReceitaRepository receitaRepository;
    private final UploadService uploadService;

    @Transactional(readOnly = true)
    public NutricionistaOutput getProfileInfos(Long userId) {
        NutricionistaOutput nutri = nutricionistaRepository.getProfileInfos(userId);
        Long diasConta = ChronoUnit.DAYS.between(nutri.getCreated().toLocalDate(), LocalDate.now());
        nutri.setDiasConta(diasConta);

        return nutri;
    }

    @Transactional(readOnly = true)
    public Nutricionista findByUserId(Long userId) {
        return nutricionistaRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public void save(NutricionistaInput nutricionistaInput) {
        UserInput userInput = nutricionistaInput.getUserInput();
        User savedUser = userService.save(userInput);

        Boolean crnValido = crnValidation.pesquisarCrn(nutricionistaInput.getCRN(),
                nutricionistaInput.getInscricao());

        if (crnValido == null) {
            throw new RuntimeException("Erro ao validar CRN");
        }

        if (Boolean.FALSE.equals(crnValido)) {
            throw new RuntimeException("CRN inválido");
        }

        DocumentosLegais termoNutri = documentosService.buscarEntidadeAtualPorTipo("TERMO_COMPROMISSO");
        aceitoTermosRepository.save(new AceitoTermos(savedUser, termoNutri));

        String crn = "" + nutricionistaInput.getCRN() + "/" + nutricionistaInput.getInscricao();

        Nutricionista nutricionista = new Nutricionista(savedUser, crn);
        nutricionistaRepository.save(nutricionista);
    }

    @Transactional
    public void cadastrarNutricionistaGoogle(String email, String nome, String foto, String crnEstado,
            String crnNumero) {

        Boolean crnValido = crnValidation.pesquisarCrn(crnEstado, crnNumero);

        if (crnValido == null) {
            throw new RuntimeException("Erro de conexão ao validar CRN. Tente novamente.");
        }

        if (Boolean.FALSE.equals(crnValido)) {
            throw new RuntimeException("CRN inválido ou inativo no conselho de nutrição.");
        }

        Role roleNutri = roleService.findById(RoleEnum.NUTRICIONISTA.getId());

        User user = new User();
        user.setEmail(email);
        user.setNome(nome);
        user.setRole(roleNutri);
        user.setActivated(true);
        user.setLoginProvider("GOOGLE");
        user.setSenha(null);

        User savedUser = userService.saveGoogleUser(user);

        DocumentosLegais termoNutri = documentosService.buscarEntidadeAtualPorTipo("TERMO_COMPROMISSO");
        aceitoTermosRepository.save(new AceitoTermos(savedUser, termoNutri));

        DocumentosLegais politicaPriv = documentosService.buscarEntidadeAtualPorTipo("POLITICA_PRIVACIDADE");
        aceitoTermosRepository.save(new AceitoTermos(savedUser, politicaPriv));

        String crnCompleto = crnEstado + "/" + crnNumero;
        Nutricionista nutricionista = new Nutricionista(savedUser, crnCompleto);
        nutricionista.setTokenConvite(UUID.randomUUID());
        nutricionista.setImagem(foto);
        nutricionistaRepository.save(nutricionista);
    }

    public PerfilNutricionistaPublicoOutput getPerfilPublico(Long id) {
        Nutricionista nutricionista = nutricionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado"));

        Long userId = nutricionista.getUser().getId();

        Long favoritadas = receitaFavoritaRepository.countByReceitaUserId(userId);
        Long avaliadas = receitaAvaliacaoRepository.countByReceitaUserId(userId);
        Long pacientes = pacienteRepository.countByNutricionistaId(id);

        long diasNoApp = ChronoUnit.DAYS.between(
                nutricionista.getUser().getCreated().toLocalDate(), LocalDate.now());

        EstatisticasNutricionistaOutput estatisticas = new EstatisticasNutricionistaOutput(
                favoritadas != null ? favoritadas : 0L,
                avaliadas != null ? avaliadas : 0L,
                pacientes != null ? pacientes : 0L,
                diasNoApp);

        List<ReceitaCardOutput> receitas = receitaRepository.findTop4PublicasByUserId(
                userId, PageRequest.of(0, 4));

        String nome = nutricionista.getUser().getNome();
        String iniciais = nome != null && !nome.isEmpty() ? nome.substring(0, 1).toUpperCase() : "";

        return new PerfilNutricionistaPublicoOutput(
                nutricionista.getId(),
                nome,
                iniciais,
                nutricionista.getCRN(),
                nutricionista.getDescPerfil(),
                estatisticas,
                receitas,
                nutricionista.getImagem());
    }

    @Transactional
    public void deactivateAccount(Long userId) {
        Nutricionista nutri = findByUserId(userId);
        nutri.setActivated(false);
    }

    @Transactional
    public void activateAccount(Long userId) {
        Nutricionista nutri = findByUserId(userId);
        nutri.setActivated(true);
    }

    @Transactional
    public String atualizarFoto(Long userId, MultipartFile foto) throws IOException {
        Nutricionista nutri = findByUserId(userId);
        String imagemAtual = nutri.getImagem();
        if (imagemAtual != null && !imagemAtual.startsWith("http")) {
            uploadService.deletarFotoPerfil(imagemAtual);
        }
        String novoNome = uploadService.saveFotoPerfil(foto);
        nutri.setImagem(novoNome);
        nutricionistaRepository.save(nutri);
        return novoNome;
    }
}