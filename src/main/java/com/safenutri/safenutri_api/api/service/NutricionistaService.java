package com.safenutri.safenutri_api.api.service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

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
import com.safenutri.safenutri_api.repository.NutricionistaRepository;
import com.safenutri.safenutri_api.repository.AceitoTermosRepository;

@Service
@RequiredArgsConstructor
public class NutricionistaService {
    private final NutricionistaRepository nutricionistaRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final CrnValidation crnValidation;
    private final DocumentosLegaisService documentosService;
    private final AceitoTermosRepository aceitoTermosRepository;

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
        user.setOnboardingCompleted(false);
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
}