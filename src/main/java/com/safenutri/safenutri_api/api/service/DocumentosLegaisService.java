package com.safenutri.safenutri_api.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safenutri.safenutri_api.model.entity.DocumentosLegais;
import com.safenutri.safenutri_api.model.input.DocumentosLegaisInput;
import com.safenutri.safenutri_api.model.output.DocumentosLegaisOutput;
import com.safenutri.safenutri_api.repository.DocumentosLegaisRepository;

@Service
@RequiredArgsConstructor
public class DocumentosLegaisService {

    private final DocumentosLegaisRepository repository;

    // Mantemos esse método interno retornando a Entidade para ser usado no
    // criarNovaVersao
    protected DocumentosLegais buscarEntidadeAtualPorTipo(String tipo) {
        return repository.findFirstByTipoAndActivatedTrueOrderByCreatedDesc(tipo)
                .orElseThrow(() -> new RuntimeException("Documento legal não configurado: " + tipo));
    }

    // Esse é o método que o Controller vai chamar no GET
    public DocumentosLegaisOutput buscarAtualPorTipo(String tipo) {
        DocumentosLegais documento = buscarEntidadeAtualPorTipo(tipo);
        return converterParaOutput(documento);
    }

    @Transactional
    public DocumentosLegaisOutput criarNovaVersao(DocumentosLegaisInput dto) {
        // 1. Busca o documento ativo atual
        DocumentosLegais documentoAtual = buscarEntidadeAtualPorTipo(dto.getTipo());

        // 2. Desativa o documento atual para manter o histórico
        documentoAtual.setActivated(false);
        repository.save(documentoAtual);

        // 3. Calcula a nova versão
        String novaVersao = calcularProximaVersao(documentoAtual.getVersao());

        // 4. Cria e salva o novo documento
        DocumentosLegais novoDocumento = new DocumentosLegais();
        novoDocumento.setTipo(dto.getTipo());
        novoDocumento.setVersao(novaVersao);
        novoDocumento.setTexto(dto.getTexto());
        novoDocumento.setActivated(true);

        DocumentosLegais documentoSalvo = repository.save(novoDocumento);

        // 5. Retorna o Output convertido
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

    // Método auxiliar para conversão
    private DocumentosLegaisOutput converterParaOutput(DocumentosLegais entity) {
        DocumentosLegaisOutput output = new DocumentosLegaisOutput();
        output.setId(entity.getId());
        output.setTipo(entity.getTipo());
        output.setVersao(entity.getVersao());
        output.setTexto(entity.getTexto());

        // Supondo que a sua DefaultEntity tenha um getCreated() que retorna
        // LocalDateTime/Date
        if (entity.getCreated() != null) {
            output.setDataCriacao(entity.getCreated().toString());
        }

        return output;
    }
}