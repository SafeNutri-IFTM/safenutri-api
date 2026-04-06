package com.safenutri.safenutri_api.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.safenutri.safenutri_api.exception.RestricaoNotFoundException;
import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.model.entity.ReceitaRestricao;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
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
    private final JwtService jwtService;
    private final RestricaoAlimentarService restricaoAlimentarService;
    private final ReceitaRestricaoService receitaRestricaoService;

    @Transactional
    public void save(ReceitaInput receitaInput) throws Exception {
        String caminhoFoto = null;

        if (receitaInput.getImagem() != null && !receitaInput.getImagem().isEmpty()) {
            caminhoFoto = uploadService.saveFotoReceita(receitaInput.getImagem());
        }

        User user = userService.getReference(jwtService.getIdUsuario());
        TipoReceita tipoReceita = tipoReceitaService.getReference(receitaInput.getTipoReceitaId());
        Boolean isPublic = false;

        if (jwtService.isNutricionista()) {
            isPublic = true;
        }
        
        Receita receita = new Receita(user, tipoReceita, receitaInput.getTitulo(), 
            receitaInput.getDescricao(), receitaInput.getTempoPreparo(), receitaInput.getPorcao(), 
            receitaInput.getCalorias(), caminhoFoto, isPublic);

        Receita savedReceita = receitaRepository.save(receita);
        if (receitaInput.getRestricoes() != null && !receitaInput.getRestricoes().isEmpty()) {
            processarRestricoes(savedReceita, receitaInput);
        }
    }

    private void processarRestricoes(Receita receita, ReceitaInput receitaInput) {
        List<Long> restricoesId = receitaInput.getRestricoes();
        Set<Long> restricoesIdSet = new HashSet<>(restricoesId);

        List<RestricaoAlimentar> restricoes = restricaoAlimentarService.findAllById(restricoesIdSet);

        if (restricoesId.size() != restricoes.size()) {
            throw new RestricaoNotFoundException("Alguma restrição não existe");
        }

        List<ReceitaRestricao> receitaRestricoes = new ArrayList<>();

        for (RestricaoAlimentar ra : restricoes) {
            ReceitaRestricao rr = new ReceitaRestricao(receita, ra);
            receitaRestricoes.add(rr);
        }

        receitaRestricaoService.saveAll(receitaRestricoes);
    }
}
