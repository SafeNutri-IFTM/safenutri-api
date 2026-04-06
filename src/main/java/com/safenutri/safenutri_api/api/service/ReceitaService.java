package com.safenutri.safenutri_api.api.service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.model.entity.TipoReceita;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.ReceitaInput;
import com.safenutri.safenutri_api.repository.ReceitaRepository;

@Service
@RequiredArgsConstructor
public class ReceitaService {
    private final ReceitaRepository receitaRepository;
    private final UploadService uploadService;
    private final TipoReceitaService tipoReceitaService;
    private final UserService userService;

    @Transactional
    public void save(ReceitaInput receitaInput) throws Exception {
        String caminhoFoto = null;

        if (receitaInput.getImagem() != null && !receitaInput.getImagem().isEmpty()) {
            caminhoFoto = uploadService.saveFotoReceita(receitaInput.getImagem());
        }

        User user = userService.getReference(receitaInput.getUserId());
        TipoReceita tipoReceita = tipoReceitaService.getReference(receitaInput.getTipoReceitaId());

        // Receita receita = new Receita(user, tipoReceita);

        Receita savedReceita = receitaRepository.save(receita);
        if (receitaInput.getRestricoes() != null && !receitaInput.getRestricoes().isEmpty()) {
            processarRestricoes(savedReceita, receitaInput);
        }
    }

    private void processarRestricoes(Receita receita, ReceitaInput receitaInput) {

    }
}
