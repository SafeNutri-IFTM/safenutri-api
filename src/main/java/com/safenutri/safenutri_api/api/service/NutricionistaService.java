package com.safenutri.safenutri_api.api.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.core.utils.CrnValidation;
import com.safenutri.safenutri_api.model.entity.AceitoTermos;
import com.safenutri.safenutri_api.model.entity.DocumentosLegais;
import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.entity.Role;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.enums.RoleEnum;
import com.safenutri.safenutri_api.model.input.NutricionistaInput;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.model.output.EstatisticasNutricionistaOutput;
import com.safenutri.safenutri_api.model.output.PerfilNutricionistaPublicoOutput;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;

import com.safenutri.safenutri_api.repository.AceitoTermosRepository;
import com.safenutri.safenutri_api.repository.NutricionistaRepository;
import com.safenutri.safenutri_api.repository.PacienteRepository;
import com.safenutri.safenutri_api.repository.ReceitaAvaliacaoRepository;
import com.safenutri.safenutri_api.repository.ReceitaFavoritaRepository;
import com.safenutri.safenutri_api.repository.ReceitaRepository;
import com.safenutri.safenutri_api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NutricionistaService {
    private final UserRepository userRepository;
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
    private final JwtService jwtService;

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

        DocumentosLegais termoNutri = documentosService.buscarAtualPorTipo("TERMO_NUTRI");
        aceitoTermosRepository.save(new AceitoTermos(savedUser, termoNutri));

        String crn = "" + nutricionistaInput.getCRN() + "/" + nutricionistaInput.getInscricao();

        Nutricionista nutricionista = new Nutricionista(savedUser, crn);
        nutricionistaRepository.save(nutricionista);
    }

    @Transactional
    public void cadastrarNutricionistaGoogle(String email, String nome, String crnEstado, String crnNumero) {

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

        DocumentosLegais termoNutri = documentosService.buscarAtualPorTipo("TERMO_NUTRI");
        aceitoTermosRepository.save(new AceitoTermos(savedUser, termoNutri));

        DocumentosLegais politicaPriv = documentosService.buscarAtualPorTipo("POLITICA_PRIVACIDADE");
        aceitoTermosRepository.save(new AceitoTermos(savedUser, politicaPriv));

        String crnCompleto = crnEstado + "/" + crnNumero;
        Nutricionista nutricionista = new Nutricionista(savedUser, crnCompleto);
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
                receitas);
    }

    public boolean deactivateAccount() {
        Long userId = jwtService.getIdUsuario();
        return nutricionistaRepository.findByUserId(userId).map(
            nutri -> {
                nutri.setActivated(false);
                nutricionistaRepository.save(nutri);

                User user = userService.findById(userId);
                user.setActivated(false);
                userRepository.save(user);

                return true;
            }
        ).orElse(false);
    }
}