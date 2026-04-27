package com.safenutri.safenutri_api.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.exception.RestricaoNotFoundException;
import com.safenutri.safenutri_api.model.entity.ModoPreparo;
import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.model.entity.ReceitaRestricao;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.entity.TipoReceita;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.ModoPreparoInput;
import com.safenutri.safenutri_api.model.input.ReceitaInput;
import com.safenutri.safenutri_api.model.output.ModoPreparoOutput;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaDetalheOutput;
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;
import com.safenutri.safenutri_api.model.output.ReceitaRestricaoOutput;
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
    private final ModoPreparoService modoPreparoService;
    private final PacienteService pacienteService;
    private final UserRestricaoService userRestricaoService;

    @Value("${filediretorioReceita}")
    private String directoryReceita;

    @Transactional
    public void save(ReceitaInput receitaInput) throws Exception {
        String caminhoFoto = null;

        if (receitaInput.getImagem() != null && !receitaInput.getImagem().isEmpty()) {
            caminhoFoto = uploadService.saveFotoReceita(receitaInput.getImagem());
        }

        User user = userService.findById(jwtService.getIdUsuario());
        TipoReceita tipoReceita = tipoReceitaService.findById(receitaInput.getTipoReceitaId());
        Boolean isPublic = false;

        if (jwtService.isNutricionista()) {
            isPublic = true;
        }

        String[] partes = receitaInput.getTempoPreparo().split(":");
        Integer horas = Integer.parseInt(partes[0]);   
        Integer minutos = Integer.parseInt(partes[1]);
        Integer tempoPreparo = (horas * 60) + minutos;
        
        Receita receita = new Receita(user, tipoReceita, receitaInput.getTitulo(), receitaInput.getDescricao(), 
            receitaInput.getIngredientes(), tempoPreparo, receitaInput.getPorcao(), receitaInput.getCalorias(), 
            caminhoFoto, isPublic
        );

        Receita savedReceita = receitaRepository.save(receita);

        List<Long> restricoesIdList = receitaInput.getRestricoes();
        if (restricoesIdList != null && !restricoesIdList.isEmpty()) {
            processarRestricoes(savedReceita, restricoesIdList);
        }

        List<ModoPreparoInput> modoPreparoInputList = receitaInput.getPassos();
        if (modoPreparoInputList != null && !modoPreparoInputList.isEmpty()) {
            processarModoPreparo(savedReceita, modoPreparoInputList);
        }
    }

    private void processarRestricoes(Receita receita, List<Long> restricoesIdList) {
        Set<Long> restricoesIdSet = new HashSet<>(restricoesIdList);

        List<RestricaoAlimentar> restricoesList = restricaoAlimentarService.findAllById(restricoesIdSet);

        if (restricoesIdSet.size() != restricoesList.size()) {
            throw new RestricaoNotFoundException("Alguma restrição não existe");
        }

        List<ReceitaRestricao> receitaRestricoes = new ArrayList<>();

        for (RestricaoAlimentar ra : restricoesList) {
            ReceitaRestricao rr = new ReceitaRestricao(receita, ra);
            receitaRestricoes.add(rr);
        }

        receitaRestricaoService.deleteByReceitaId(receita.getId());
        receitaRestricaoService.saveAll(receitaRestricoes);
    }

    private void processarModoPreparo(Receita receita, List<ModoPreparoInput> modoPreparoInputList) {
        List<ModoPreparo> mpList = new ArrayList<>();
        Long passo = 1L;

        for (ModoPreparoInput mpi : modoPreparoInputList) {
            ModoPreparo mp = new ModoPreparo(receita, passo, mpi.getTituloEtapa(), mpi.getDescricaoEtapa());
            mpList.add(mp);
            passo++;
        }

        modoPreparoService.deleteByReceitaId(receita.getId());
        modoPreparoService.saveAll(mpList);
    }

    public void desativar(Long id) {
        Receita receita = findById(id);
        receita.setActivated(false);
        receitaRepository.save(receita);
    }

        public Page<ReceitaCardOutput> getMinhasReceitas(int page, int size) {
        Long userId = jwtService.getIdUsuario();
        Pageable pageable = PageRequest.of(page, size);

        return receitaRepository.getMinhasReceitas(userId, pageable);
    }

    public Page<ReceitaCardOutput> findReceitasByRestricoesId(Long userId, int page, int size) {
        User user = userService.findById(userId);
        List<Long> restricoesIds = userRestricaoService.findAllByUserId(userId);
        Long qntd = (long) restricoesIds.size();
        Pageable pageable = PageRequest.of(page, size);

        List<Long> nutricionistaIds = pacienteService.findNutricionistaIdByUserId(user.getId());

        if (!nutricionistaIds.isEmpty()) {
            return receitaRepository.findByRestricoesIdAndNutricionistaId(restricoesIds, qntd, nutricionistaIds, pageable);
        } else {
            return receitaRepository.findByRestricoesId(restricoesIds, qntd, pageable);
        }
    }

    public List<ReceitaCardOutput> findTop3Filtrado(Long userId) {
        List<Long> restricoesIds = userRestricaoService.findAllByUserId(userId);
        Long qntd = (long) restricoesIds.size();
        
        return receitaRepository.findTop3ByRestricoesIds(restricoesIds, qntd, PageRequest.of(0, 3));
    }

    public Long countByNutricionistaId(Long nutricionistaId) {
        return receitaRepository.countByNutricionistaId(nutricionistaId);
    }

    public List<ReceitaPieChartOutput> countGroupByRestricoes(Long nutricionistaId) {
        return receitaRepository.countByNutricionistaIdAndGroupByRestricoes(nutricionistaId);
    }

    public List<ReceitaCardOutput> findTop3() {
        return receitaRepository.findTop3(PageRequest.of(0, 3));
    }

    public ReceitaDetalheOutput findDetalheById(Long id) {
        Receita receita = receitaRepository.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        List<ModoPreparoOutput> passos = modoPreparoService.findByReceitaId(id);
        List<ReceitaRestricaoOutput> restricoes = receitaRestricaoService.findByReceitaId(id);

        return new ReceitaDetalheOutput(receita, passos, restricoes);
    }

    public Receita findById(Long id) {
        return receitaRepository.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada"));
    }

    // ========================================================================================
    // NOVO MÉTODO DA TASK SN-110: BUSCA PAGINADA PARA O FEED
    // ========================================================================================
    public Page<ReceitaCardOutput> buscarReceitasPaginadas(String termo, int page, int size) {
        // Previne NullPointerException caso não passem termo na requisição
        String termoBusca = (termo != null) ? termo : "";
        
        // Cria o objeto nativo do Spring Data para realizar a paginação (LIMIT e OFFSET)
        Pageable pageable = PageRequest.of(page, size);
        
        // Chama a query que criámos no ReceitaRepository
        return receitaRepository.findByTituloPaginado(termoBusca, pageable);
    }
    
}
