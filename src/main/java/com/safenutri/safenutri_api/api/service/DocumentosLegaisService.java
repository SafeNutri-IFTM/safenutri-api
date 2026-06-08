package com.safenutri.safenutri_api.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safenutri.safenutri_api.model.entity.AceitoTermos;
import com.safenutri.safenutri_api.model.entity.DocumentosLegais;
import com.safenutri.safenutri_api.model.input.DocumentosLegaisInput;
import com.safenutri.safenutri_api.model.output.DocumentosLegaisOutput;
import com.safenutri.safenutri_api.repository.AceitoTermosRepository;
import com.safenutri.safenutri_api.repository.DocumentosLegaisRepository;
import com.safenutri.safenutri_api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class DocumentosLegaisService {

    private final DocumentosLegaisRepository repository;
    private final AceitoTermosRepository aceitoTermosRepository;
    private final UserRepository userRepository;

    protected DocumentosLegais buscarEntidadeAtualPorTipo(String tipo) {
        return repository.findFirstByTipoAndActivatedTrueOrderByCreatedDesc(tipo)
                .orElseThrow(() -> new RuntimeException("Documento legal não configurado: " + tipo));
    }

    public DocumentosLegaisOutput buscarAtualPorTipo(String tipo) {
        DocumentosLegais documento = buscarEntidadeAtualPorTipo(tipo);
        return converterParaOutput(documento);
    }

    @Transactional
    public DocumentosLegaisOutput criarNovaVersao(DocumentosLegaisInput dto) {
        DocumentosLegais documentoAtual = buscarEntidadeAtualPorTipo(dto.getTipo());

        documentoAtual.setActivated(false);
        repository.save(documentoAtual);

        String novaVersao = calcularProximaVersao(documentoAtual.getVersao());

        DocumentosLegais novoDocumento = new DocumentosLegais();
        novoDocumento.setTipo(dto.getTipo());
        novoDocumento.setVersao(novaVersao);
        novoDocumento.setTexto(dto.getTexto());
        novoDocumento.setActivated(true);

        DocumentosLegais documentoSalvo = repository.save(novoDocumento);

        return converterParaOutput(documentoSalvo);
    }

    private String calcularProximaVersao(String versaoAtual) {
        try {
            String[] partes = versaoAtual.split("\\.");
            int versaoMaior = Integer.parseInt(partes[0]);
            return (versaoMaior + 1) + ".0";
        } catch (Exception e) {
            return versaoAtual + ".1";
        }
    }

    private DocumentosLegaisOutput converterParaOutput(DocumentosLegais entity) {
        DocumentosLegaisOutput output = new DocumentosLegaisOutput();
        output.setId(entity.getId());
        output.setTipo(entity.getTipo());
        output.setVersao(entity.getVersao());
        output.setTexto(entity.getTexto());

        if (entity.getCreated() != null) {
            output.setDataCriacao(entity.getCreated().toString());
        }

        return output;
    }

    @Transactional
    public void registrarAceiteUsuario(Long userId, String tipo) {
        DocumentosLegais documentoAtual = buscarEntidadeAtualPorTipo(tipo);

        com.safenutri.safenutri_api.model.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean jaAceitou = aceitoTermosRepository.existsByUserIdAndDocumentoId(userId, documentoAtual.getId());

        if (!jaAceitou) {
            AceitoTermos novoAceite = new AceitoTermos(user, documentoAtual);
            aceitoTermosRepository.save(novoAceite);
        }
    }
}