package com.safenutri.safenutri_api.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.safenutri.safenutri_api.core.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.safenutri.safenutri_api.model.output.*;

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
    private final RestricaoAlimentarService restricaoAlimentarService;
    private final ReceitaRestricaoService receitaRestricaoService;
    private final ModoPreparoService modoPreparoService;
    private final PacienteService pacienteService;
    private final UserRestricaoService userRestricaoService;
    private final JwtService jwtService;
    
    // Repositorios unidos (Avaliação + Favoritos)
    private final UserRepository userRepository;
    private final ReceitaAvaliacaoRepository receitaAvaliacaoRepository;
    private final ReceitaFavoritaRepository receitaFavoritaRepository;
    private final NutricionistaRepository nutricionistaRepository;
    private final StatusDenunciaRepository statusDenunciaRepository;

    @Value("${filediretorioReceita}")
    private String directoryReceita;

    @Transactional
    public void save(Long userId, ReceitaInput receitaInput) throws Exception {
        String caminhoFoto = null;

        if (receitaInput.getImagem() != null && !receitaInput.getImagem().isEmpty()) {
            caminhoFoto = uploadService.saveFotoReceita(receitaInput.getImagem());
        }

        User user = userService.findById(userId);
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
                caminhoFoto, isPublic);

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
    public void update(Long userId, ReceitaInput receitaInput, Long id) throws Exception {
        Receita receitaExistente = receitaRepository.findById(id)
                .orElseThrow(() -> new ReceitaNotFoundException("Receita não encontrada"));

        User user = userService.findById(userId);

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

        // Vamos buscar o status de "EM ANÁLISE" no banco (assumindo que o ID seja 3, por exemplo, ou buscar por nome)
        StatusDenuncia statusEmAnalise = statusDenunciaRepository.findByStatus("EM ANÁLISE")
                .orElseThrow(() -> new RuntimeException("Status EM ANÁLISE não cadastrado no BD"));

        // Buscar todas as denúncias desta receita que estão SUSPENSAS
        List<ReceitaDenuncia> denunciasSuspensas = receitaDenunciaRepository.findAllByReceitaIdAndStatus_Status(id, "SUSPENSA");
        
        // Mudar o status de todas elas para "EM ANÁLISE"
        if (!denunciasSuspensas.isEmpty()) {
            for (ReceitaDenuncia denuncia : denunciasSuspensas) {
                denuncia.setStatus(statusEmAnalise);
            }
            receitaDenunciaRepository.saveAll(denunciasSuspensas);
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

    @Transactional
    public void desativarReceita(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new ReceitaNotFoundException("Receita não encontrada"));
        receita.setActivated(false);
        receitaRepository.save(receita);
    }

    @Transactional
    public void ativarReceita(Long id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new ReceitaNotFoundException("Receita não encontrada"));
        receita.setActivated(true);
        receitaRepository.save(receita);
    }

    @Transactional(readOnly = true)
    public Page<ReceitaCardOutput> getMinhasReceitas(Long userId, String termo, Long tipoReceitaId, Boolean activated, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReceitaCardOutput> rcoPage = receitaRepository.getMinhasReceitas(termo, tipoReceitaId, activated, userId, pageable);

        return rcoPage.map(r -> {
            String tempoFormato = formatarTempo(r.getTempo());
            r.setTempoFormatado(tempoFormato);

            // --- INÍCIO DA INTEGRAÇÃO COM O FIGMA ---
            Receita receita = receitaRepository.findById(r.getId()).orElse(null);
            
            if (receita != null) {
                // Se a receita estiver INATIVA, vamos ver qual é o status dos Reports
                if (r.getActivated() != null && !r.getActivated()) {
                    List<ReceitaDenuncia> denuncias = receitaDenunciaRepository.findByReceitaId(r.getId());
                    
                    if (denuncias != null && !denuncias.isEmpty()) {
                        String statusPrioritario = null;
                        int prioridadeMaxima = 99; // Quanto menor o número, maior a gravidade
                        
                        // =====================================================================
                        // 1º PASSO: Descobre qual é o status principal da receita (O Badge)
                        // =====================================================================
                        for (ReceitaDenuncia d : denuncias) {
                            if (d.getStatus() != null) {
                                String statusAtual = d.getStatus().getStatus().toUpperCase();
                                int prioridadeAtual = 99;
                                
                                if (statusAtual.equals("SUSPENSA") || statusAtual.equals("EM CORREÇÃO")) {
                                    prioridadeAtual = 1;
                                } else if (statusAtual.equals("EM ANÁLISE") || statusAtual.equals("EM ANALISE")) {
                                    prioridadeAtual = 2;
                                } else if (statusAtual.equals("PENDENTE") || statusAtual.equals("AGUARDANDO")) {
                                    prioridadeAtual = 3;
                                }

                                if (prioridadeAtual < prioridadeMaxima) {
                                    prioridadeMaxima = prioridadeAtual;
                                    statusPrioritario = statusAtual;
                                }
                            }
                        }

                        // =====================================================================
                        // 2º PASSO: Junta APENAS os motivos que batem com o status principal!
                        // =====================================================================
                        StringBuilder motivosAgrupados = new StringBuilder();
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        for (ReceitaDenuncia d : denuncias) {
                            if (d.getStatus() != null && statusPrioritario != null) {
                                String statusAtual = d.getStatus().getStatus().toUpperCase();
                                
                                // FILTRO MÁGICO 2.0: Se a receita está Suspensa, mostra Suspensos. 
                                // Se a receita está Em Análise, mostra Em Análise!
                                if (statusAtual.equals(statusPrioritario)) {
                                    if (d.getDescricao() != null && !d.getDescricao().trim().isEmpty()) {
                                        String dataFormatada = "";
                                        
                                        if (d.getCreated() != null) {
                                            try {
                                                dataFormatada = d.getCreated().format(formatter) + " - ";
                                            } catch (Exception e) {
                                                dataFormatada = d.getCreated().toString() + " - ";
                                            }
                                        }
                                        
                                        motivosAgrupados.append("• ")
                                                        .append(dataFormatada)
                                                        .append(d.getDescricao())
                                                        .append("\n\n");
                                    }
                                }
                            }
                        }

                        // Envia para o Angular o status mais grave e os motivos perfeitamente filtrados!
                        if (statusPrioritario != null) {
                            r.setStatusDenuncia(statusPrioritario);
                            r.setMotivoDenuncia(motivosAgrupados.toString().trim()); 
                        }
                    }
                }
            }
            // --- FIM DA INTEGRAÇÃO COM O FIGMA ---

            return r;
        });
    }

    public Page<ReceitaCardOutput> findReceitasByRestricoesId(Long userId, int page, int size) {
        User user = userService.findById(userId);
        List<Long> restricoesIds = userRestricaoService.findAllByUserId(userId);
        Pageable pageable = PageRequest.of(page, size);

        List<Long> nutricionistaIds = pacienteService.findNutricionistaIdByUserId(user.getId());
        Page<ReceitaCardOutput> rcoPage;

        if (restricoesIds.isEmpty()) {
            rcoPage = receitaRepository.findReceitasForUserWithNoRestrictions(userId, pageable);
        } else {

            if (!nutricionistaIds.isEmpty()) {
                rcoPage = receitaRepository.findByRestricoesIdAndNutricionistaId(restricoesIds, nutricionistaIds, userId, pageable);
            } else {
                rcoPage = receitaRepository.findByRestricoesId(restricoesIds, userId, pageable);
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
        Page<ReceitaCardOutput> rcoPage;

        rcoPage = receitaRepository.findReceitasAvaliadasByUserId(userId, termoBusca, pageable);

        return rcoPage.map(r -> {
            String tempoFormato = formatarTempo(r.getTempo());
            r.setTempoFormatado(tempoFormato);
            return r;
        });
    }

    public List<ReceitaCardOutput> getTop3Filtrado(Long userId) {
        List<Long> restricoesIds = userRestricaoService.findAllByUserId(userId);
        List<ReceitaCardOutput> rcoList;

        if (restricoesIds.isEmpty()) {
            rcoList = receitaRepository.findTop3ReceitasForUserWithNoRestrictions(userId, PageRequest.of(0, 3));
        } else {
            rcoList = receitaRepository.findTop3ByRestricoesIds(restricoesIds, userId, PageRequest.of(0, 3));
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

    public List<ReceitaCardOutput> getTop3() {
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

    @Transactional(readOnly = true)
    public ReceitaDetalheOutput findDetalheById(Long id) {
        ReceitaOutput receita = receitaRepository.findReceita(id);
        List<ModoPreparoOutput> passos = modoPreparoService.findByReceitaId(id);
        List<RestricaoOutput> restricoes = receitaRestricaoService.findByReceitaId(id);

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
        List<RestricaoOutput> receitaRestricoes = receitaRestricaoRepository.findByReceitaId(id);
        
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
                            
                            Optional<Nutricionista> nutricionista = nutricionistaRepository.findByUserId(aval.getUser().getId());
                            if (nutricionista.isPresent()) {
                                map.put("nutricionistaId", nutricionista.get().getId());
                                map.put("imagem", nutricionista.get().getImagem());
                            } else {
                                map.put("nutricionistaId", 1L); // fallback seguro se o usuário não tiver nutricionista
                                map.put("imagem", null);
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

    public Map<String, Object> getMinhaAvaliacao(Long receitaId, Long userId) {
        User user = userService.findById(userId);
        
        return receitaAvaliacaoRepository.findByReceitaIdAndUserId(receitaId, user.getId())
                .map(aval -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("nota", aval.getQntdEstrela());
                    response.put("comentario", aval.getDescricao());
                    return response;
                }).orElse(null);
    }

    public void avaliar(Long userId, Long receitaId, Integer nota, String comentario) {
        User user = userService.findById(userId);

        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada."));

        ReceitaAvaliacao avaliacao = receitaAvaliacaoRepository.findByReceitaIdAndUserId(receitaId, userId)
                .orElse(new ReceitaAvaliacao());

        avaliacao.setReceita(receita);
        avaliacao.setUser(user);
        avaliacao.setQntdEstrela(Double.valueOf(nota));
        avaliacao.setDescricao(comentario);

        receitaAvaliacaoRepository.save(avaliacao);
    }

    @Transactional
    public void favoritar(Long userId, Long receitaId) {
        if (receitaFavoritaRepository.existsByUserIdAndReceitaId(userId, receitaId)) {
            throw new RuntimeException("Você já favoritou esta receita.");
        }

        User user = userService.findById(userId);
        Receita receita = findById(receitaId);

        ReceitaFavorita favorita = new ReceitaFavorita(user, receita);
        receitaFavoritaRepository.save(favorita);
    }

    public Page<ReceitaCardOutput> getFavoritas(Long userId, int page, int size, String termo) {
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
    public void desfavoritar(Long userId, Long receitaId) {
        if (!receitaFavoritaRepository.existsByUserIdAndReceitaId(userId, receitaId)) {
            throw new RuntimeException("Esta receita não está na sua lista de favoritos.");
        }

        receitaFavoritaRepository.deleteByUserIdAndReceitaId(userId, receitaId);
    }

    public void denunciar(Long receitaId, Long userId, String motivo) {
        User user = userService.findById(userId);

        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada."));

        StatusDenuncia statusPendente = statusDenunciaRepository.findById(1L)
                .orElseGet(() -> {
                    StatusDenuncia novoStatus = new StatusDenuncia();
                    novoStatus.setStatus("PENDENTE");
                    return statusDenunciaRepository.save(novoStatus);
                });

        ReceitaDenuncia denuncia = new ReceitaDenuncia(user, receita, motivo, statusPendente);

        receitaDenunciaRepository.save(denuncia);
    }

    public Long countAll() {
        return receitaRepository.countAll();
    }
}