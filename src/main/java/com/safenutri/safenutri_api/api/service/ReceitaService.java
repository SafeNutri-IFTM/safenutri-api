package com.safenutri.safenutri_api.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.exception.ReceitaNotFoundException;
import com.safenutri.safenutri_api.exception.RestricaoNotFoundException;
import com.safenutri.safenutri_api.model.entity.ModoPreparo;
import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.model.entity.ReceitaAvaliacao;
import com.safenutri.safenutri_api.model.entity.ReceitaDenuncia;
import com.safenutri.safenutri_api.model.entity.ReceitaFavorita;
import com.safenutri.safenutri_api.model.entity.ReceitaRestricao;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.entity.StatusDenuncia;
import com.safenutri.safenutri_api.model.entity.TipoReceita;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.ModoPreparoInput;
import com.safenutri.safenutri_api.model.input.ReceitaInput;
import com.safenutri.safenutri_api.model.output.ModoPreparoOutput;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaDetalheOutput;
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;
import com.safenutri.safenutri_api.model.output.ReceitaRestricaoOutput;

import com.safenutri.safenutri_api.repository.ModoPreparoRepository;
import com.safenutri.safenutri_api.repository.ReceitaAvaliacaoRepository;
import com.safenutri.safenutri_api.repository.ReceitaDenunciaRepository;
import com.safenutri.safenutri_api.repository.ReceitaFavoritaRepository;
import com.safenutri.safenutri_api.repository.ReceitaRepository;
import com.safenutri.safenutri_api.repository.ReceitaRestricaoRepository;
import com.safenutri.safenutri_api.repository.UserRepository;
import com.safenutri.safenutri_api.repository.NutricionistaRepository;
import com.safenutri.safenutri_api.repository.StatusDenunciaRepository;
import com.safenutri.safenutri_api.model.entity.Nutricionista;

@Service
@RequiredArgsConstructor
public class ReceitaService {
    private final ReceitaRepository receitaRepository;
    private final ModoPreparoRepository modoPreparoRepository;
    private final ReceitaRestricaoRepository receitaRestricaoRepository;
    private final ReceitaDenunciaRepository receitaDenunciaRepository;
    private final UploadService uploadService;
    private final TipoReceitaService tipoReceitaService;
    private final UserService userService;
    private final JwtService jwtService;
    private final RestricaoAlimentarService restricaoAlimentarService;
    private final ReceitaRestricaoService receitaRestricaoService;
    private final ModoPreparoService modoPreparoService;
    private final PacienteService pacienteService;
    private final UserRestricaoService userRestricaoService;
    
    // Repositorios unidos (Avaliação + Favoritos)
    private final UserRepository userRepository;
    private final ReceitaAvaliacaoRepository receitaAvaliacaoRepository;
    private final ReceitaFavoritaRepository receitaFavoritaRepository;
    private final NutricionistaRepository nutricionistaRepository;
    private final StatusDenunciaRepository statusDenunciaRepository;

    @Value("${filediretorioReceita}")
    private String directoryReceita;

    @Transactional
    public void save(ReceitaInput receitaInput) throws Exception {
        String caminhoFoto = null;

        if (receitaInput.getImagem() != null && !receitaInput.getImagem().isEmpty()) {
            caminhoFoto = uploadService.saveFotoReceita(receitaInput.getImagem()); // <-- CORRIGIDO AQUI
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
                caminhoFoto, isPublic); // <-- CORRIGIDO AQUI

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

    @Transactional
    public void update(ReceitaInput receitaInput, Long id) throws Exception {
        Receita receitaExistente = receitaRepository.findById(id)
                .orElseThrow(() -> new ReceitaNotFoundException("Receita não encontrada"));

        User user = userService.findById(jwtService.getIdUsuario());

        if (!receitaExistente.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Você não tem permissão para alterar esta receita.");
        }

        boolean novaImagemEnviada = receitaInput.getImagem() != null && !receitaInput.getImagem().isEmpty();

        if (novaImagemEnviada) {
            if (receitaExistente.getImagem() != null) {
                uploadService.deletarFotoReceita(receitaExistente.getImagem());
            }

            String novoNome = uploadService.saveFotoReceita(receitaInput.getImagem());
            receitaExistente.setImagem(novoNome);
        } else {
            if (user.getRole().getRole().equals("NUTRICIONISTA") && receitaExistente.getImagem() == null) {
                throw new RuntimeException("Nutricionistas devem obrigatoriamente incluir uma imagem.");
            }
        }

        TipoReceita tipoReceita = tipoReceitaService.findById(receitaInput.getTipoReceitaId());
        receitaExistente.setTipoReceita(tipoReceita);

        Boolean isPublic = false;

        if (jwtService.isNutricionista()) {
            isPublic = true;
        }

        String[] partes = receitaInput.getTempoPreparo().split(":");
        Integer horas = Integer.parseInt(partes[0]);
        Integer minutos = Integer.parseInt(partes[1]);
        Integer tempoPreparo = (horas * 60) + minutos;

        receitaExistente.setTitulo(receitaInput.getTitulo());
        receitaExistente.setDescricao(receitaInput.getDescricao());
        receitaExistente.setTempoPreparo(tempoPreparo);
        receitaExistente.setPorcao(receitaInput.getPorcao());
        receitaExistente.setCalorias(receitaInput.getCalorias());
        receitaExistente.setIngredientes(receitaInput.getIngredientes());
        receitaExistente.setPublicBoolean(isPublic);

        Receita savedReceita = receitaRepository.save(receitaExistente);

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

    private String formatarTempo(Integer minutos) {
        Integer horas = minutos / 60;
        Integer min = minutos % 60;

        if (horas > 0 && min > 0) {
            return horas + "hr " + min + "min";
        } else if (horas > 0) {
            return horas + "hr";
        } else {
            return min + "min";
        }
    }

    public void alternarStatus(Long id) {
        Receita receita = findById(id);
        receita.setActivated(!receita.getActivated());
        receitaRepository.save(receita);
    }

    public Page<ReceitaCardOutput> getMinhasReceitas(String termo, Long tipoReceitaId, Boolean activated, int page, int size) {
        Long userId = jwtService.getIdUsuario();
        Pageable pageable = PageRequest.of(page, size);

        Page<ReceitaCardOutput> rcoPage = receitaRepository.getMinhasReceitas(termo, tipoReceitaId, activated, userId, pageable);

        return rcoPage.map(r -> {
            String tempoFormato = formatarTempo(r.getTempo());
            r.setTempoFormatado(tempoFormato);
            return r;
        });
    }

    public Page<ReceitaCardOutput> findReceitasByRestricoesId(Long userId, int page, int size) {
        User user = userService.findById(userId);
        List<Long> restricoesIds = userRestricaoService.findAllByUserId(userId);
        Long qntd = (long) restricoesIds.size();
        Pageable pageable = PageRequest.of(page, size);

        List<Long> nutricionistaIds = pacienteService.findNutricionistaIdByUserId(user.getId());
        Page<ReceitaCardOutput> rcoPage;

        if (restricoesIds.isEmpty()) {
            rcoPage = receitaRepository.findReceitasForUserWithNoRestrictions(userId, pageable);
        } else {

            if (!nutricionistaIds.isEmpty()) {
                rcoPage = receitaRepository.findByRestricoesIdAndNutricionistaId(restricoesIds, qntd, nutricionistaIds, userId, pageable);
            } else {
                rcoPage = receitaRepository.findByRestricoesId(restricoesIds, qntd, userId, pageable);
            }
        }

        return rcoPage.map(r -> {
            String tempoFormato = formatarTempo(r.getTempo());
            r.setTempoFormatado(tempoFormato);
            return r;
        });
    }

    public Page<ReceitaCardOutput> getReceitasAvaliadas(Long userId, int page, int size, String termo) {
        Pageable pageable = PageRequest.of(page, size);
        String termoBusca = (termo != null) ? termo : "";
        return receitaRepository.findReceitasAvaliadasByUserId(userId, termoBusca, pageable);
    }

    public List<ReceitaCardOutput> findTop3Filtrado(Long userId) {
        List<Long> restricoesIds = userRestricaoService.findAllByUserId(userId);
        Long qntd = (long) restricoesIds.size();
        List<ReceitaCardOutput> rcoList;

        if (restricoesIds.isEmpty()) {
            rcoList = receitaRepository.findTop3ReceitasForUserWithNoRestrictions(userId, PageRequest.of(0, 3));
        } else {
            rcoList = receitaRepository.findTop3ByRestricoesIds(restricoesIds, qntd, userId, PageRequest.of(0, 3));
        }

        return rcoList.stream()
            .map(r -> {
                String tempoFormato = formatarTempo(r.getTempo());
                r.setTempoFormatado(tempoFormato);
                return r;
            })
        .toList();
    }

    public Long countByNutricionistaId(Long nutricionistaId) {
        return receitaRepository.countByNutricionistaId(nutricionistaId);
    }

    public List<ReceitaPieChartOutput> countGroupByRestricoes(Long nutricionistaId) {
        return receitaRepository.countByNutricionistaIdAndGroupByRestricoes(nutricionistaId);
    }

    public List<ReceitaCardOutput> findTop3() {
        Long userId = jwtService.getIdUsuario();
        List<ReceitaCardOutput> rcoList;

        rcoList = receitaRepository.findTop3(userId, PageRequest.of(0, 3));

        return rcoList.stream()
            .map(r -> {
                String tempoFormato = formatarTempo(r.getTempo());
                r.setTempoFormatado(tempoFormato);
                return r;
            })
        .toList();
    }

    public ReceitaDetalheOutput findDetalheById(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        List<ModoPreparoOutput> passos = modoPreparoService.findByReceitaId(id);
        List<ReceitaRestricaoOutput> restricoes = receitaRestricaoService.findByReceitaId(id);

        return new ReceitaDetalheOutput(receita, passos, restricoes);
    }

    public Receita findById(Long id) {
        return receitaRepository.findById(id).orElseThrow(() -> new RuntimeException("Receita não encontrada"));
    }

    public Page<ReceitaCardOutput> buscarReceitasPaginadas(String termo, int page, int size) {
        String termoBusca = (termo != null) ? termo : "";
        Long userId = jwtService.getIdUsuario();

        Pageable pageable = PageRequest.of(page, size);
        Page<ReceitaCardOutput> rcoPage;

        rcoPage = receitaRepository.findByTituloPaginado(termoBusca, userId, pageable);

        return rcoPage.map(r -> {
            String tempoFormato = formatarTempo(r.getTempo());
            r.setTempoFormatado(tempoFormato);
            return r;
        });
    }

    @Transactional 
    public Map<String, Object> getReceitaCompletaById(Long id) {
        Receita receita = receitaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Receita não encontrada com ID: " + id));

        List<ModoPreparoOutput> passos = modoPreparoRepository.findByReceitaId(id);
        List<ReceitaRestricaoOutput> receitaRestricoes = receitaRestricaoRepository.findByReceitaId(id);
        
        List<ReceitaAvaliacao> avaliacoes = receitaAvaliacaoRepository.findByReceitaIdOrderByIdDesc(id);
        List<Map<String, Object>> avaliacoesNutri = new ArrayList<>();
        
        double somaNutri = 0.0;
        int qtdNutri = 0;
        double somaUser = 0.0;
        int qtdUser = 0;
        
        for (ReceitaAvaliacao aval : avaliacoes) {
            try {
                if (aval.getUser() != null && aval.getUser().getRole() != null) {
                    
                    String userRole = aval.getUser().getRole().getRole();
                    Double nota = aval.getQntdEstrela() != null ? aval.getQntdEstrela() : 0.0;
                    
                    if (userRole != null && userRole.toUpperCase().equals("NUTRICIONISTA")) {
                        somaNutri += nota;
                        qtdNutri++;
                        
                        if (aval.getDescricao() != null && !aval.getDescricao().trim().isEmpty()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("nome", aval.getUser().getNome()); 
                            map.put("nota", aval.getQntdEstrela());
                            map.put("comentario", aval.getDescricao());
                            map.put("avatar", null); 
                            
                            Nutricionista nutricionista = nutricionistaRepository.findByUserId(aval.getUser().getId());
                            if (nutricionista != null) {
                                map.put("nutricionistaId", nutricionista.getId());
                            } else {
                                map.put("nutricionistaId", 1L); // fallback seguro se o usuário não tiver nutricionista
                            }
                            
                            avaliacoesNutri.add(map);
                        }
                    } else {
                        somaUser += nota;
                        qtdUser++;
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar uma avaliação: " + e.getMessage());
            }
        }

        double mediaNutri = qtdNutri > 0 ? (somaNutri / qtdNutri) : 0.0;
        double mediaUser = qtdUser > 0 ? (somaUser / qtdUser) : 0.0;

        Map<String, Object> response = new HashMap<>();
        response.put("receita", receita);
        response.put("passos", passos);
        response.put("restricoes", receitaRestricoes);
        response.put("avaliacoesNutricionistas", avaliacoesNutri);
        
        response.put("mediaNutricionistas", mediaNutri);
        response.put("qtdAvaliacoesNutri", qtdNutri);
        response.put("mediaUsuarios", mediaUser);
        response.put("qtdAvaliacoesUser", qtdUser);
        
        return response;
    }

    public Map<String, Object> getMinhaAvaliacao(Long receitaId, String emailUser) {
        User user = userRepository.findByEmail(emailUser)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        
        return receitaAvaliacaoRepository.findByReceitaIdAndUserId(receitaId, user.getId())
                .map(aval -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("nota", aval.getQntdEstrela());
                    response.put("comentario", aval.getDescricao());
                    return response;
                }).orElse(null);
    }

    public void avaliar(Long receitaId, String emailUser, Integer nota, String comentario) {
        User user = userRepository.findByEmail(emailUser)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada."));

        ReceitaAvaliacao avaliacao = receitaAvaliacaoRepository.findByReceitaIdAndUserId(receitaId, user.getId())
                .orElse(new ReceitaAvaliacao());

        avaliacao.setReceita(receita);
        avaliacao.setUser(user);
        avaliacao.setQntdEstrela(Double.valueOf(nota));
        avaliacao.setDescricao(comentario);

        receitaAvaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public void favoritar(Long receitaId) {
        Long userId = jwtService.getIdUsuario();

        if (receitaFavoritaRepository.existsByUserIdAndReceitaId(userId, receitaId)) {
            throw new RuntimeException("Você já favoritou esta receita.");
        }

        User user = userService.findById(userId);
        Receita receita = findById(receitaId);

        ReceitaFavorita favorita = new ReceitaFavorita(user, receita);
        receitaFavoritaRepository.save(favorita);
    }

    public Page<ReceitaCardOutput> getFavoritas(int page, int size, String termo) {
        Long userId = jwtService.getIdUsuario();
        Pageable pageable = PageRequest.of(page, size);
        String termoBusca = (termo != null) ? termo : "";
        Page<ReceitaCardOutput> rcoPage;

        rcoPage = receitaFavoritaRepository.findFavoritasByUserId(userId, termoBusca, pageable);

        return rcoPage.map(r -> {
            String tempoFormato = formatarTempo(r.getTempo());
            r.setTempoFormatado(tempoFormato);
            return r;
        });
    }

    @Transactional
    public void desfavoritar(Long receitaId) {
        Long userId = jwtService.getIdUsuario();

        if (!receitaFavoritaRepository.existsByUserIdAndReceitaId(userId, receitaId)) {
            throw new RuntimeException("Esta receita não está na sua lista de favoritos.");
        }

        receitaFavoritaRepository.deleteByUserIdAndReceitaId(userId, receitaId);
    }

    public void denunciar(Long receitaId, String emailUsuario, String motivo) {
        // 1. Busca o utilizador logado que disparou a denúncia
        User user = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado."));

        // 2. Busca a receita que está a ser denunciada
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada."));

        // 3. Cria e popula a entidade de denúncia
        ReceitaDenuncia denuncia = new ReceitaDenuncia();
        denuncia.setReceita(receita);
        denuncia.setUser(user);
        
        // AQUI ESTÁ A CORREÇÃO DO ERRO NO VS CODE
        denuncia.setDescricao(motivo); 

        // Tenta buscar o status 1. Se não existir, ele cria, salva no banco e depois usa.
        StatusDenuncia statusPendente = statusDenunciaRepository.findById(1L)
                .orElseGet(() -> {
                    StatusDenuncia novoStatus = new StatusDenuncia();
                    // novoStatus.setId(1L); // Descomente se o ID não for auto-incremento
                    novoStatus.setStatus("PENDENTE");
                    return statusDenunciaRepository.save(novoStatus);
                });
        
        denuncia.setStatus(statusPendente);

        // 4. Salva na base de dados
        receitaDenunciaRepository.save(denuncia);
    }
}