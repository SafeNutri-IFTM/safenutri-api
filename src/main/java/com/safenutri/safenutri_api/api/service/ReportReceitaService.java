package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.output.ReportReceitaDetalheOutput;
import com.safenutri.safenutri_api.model.output.ReportReceitaOutput;
import com.safenutri.safenutri_api.repository.ReceitaDenunciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportReceitaService {

    private final ReceitaDenunciaRepository denunciaRepository;

    public ReportReceitaService(ReceitaDenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    @Transactional(readOnly = true)
    public Page<ReportReceitaOutput> listarTodosPaginado(int page, int size, boolean ativo) {
        // Passando a variável 'ativo' para o repository filtrar os dados corretos
        return denunciaRepository.findAllReports(PageRequest.of(page, size), ativo);
    }

    @Transactional(readOnly = true)
    public ReportReceitaDetalheOutput buscarDetalhePorId(Long id) {
        return denunciaRepository.findReportDetalheById(id);
    }

    @Transactional
    public void excluirReport(Long id) {
        denunciaRepository.deleteById(id);
    }

    @Transactional
    public void inativarReceitaPorReport(Long idReport) {
        // 1. Muda a coluna activated para false na tabela receita
        denunciaRepository.inativarReceitaPorReport(idReport);
        
        // 2. Muda a coluna do status da denúncia para id = 4 (Corrigindo)
        denunciaRepository.atualizarStatusReportParaCorrigindo(idReport);
    }
}