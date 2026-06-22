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
        denunciaRepository.inativarReceitaPorReport(idReport);
        // Suspende TODOS os relatórios vinculados à receita
        denunciaRepository.atualizarTodosStatusReportParaSuspensa(idReport);
    }

    @Transactional
    public void aprovarReport(Long idReport) {
        denunciaRepository.atualizarStatusReportParaConcluido(idReport);
        
        long restantes = denunciaRepository.countReportsNaoConcluidosPorReportId(idReport);
        
        if (restantes == 0) {
            denunciaRepository.ativarReceitaPorReport(idReport);
        }
    }

    @Transactional
    public void recusarReport(Long idReport) {
        // 1. SOLUÇÃO DO BUG: Força explicitamente a receita a ficar INATIVA (activated = false)
        denunciaRepository.inativarReceitaPorReport(idReport);
        
        // 2. SOLUÇÃO DO BUG: Atualiza individualmente APENAS o report recusado para o status Suspensa
        denunciaRepository.atualizarStatusReportParaSuspensaIndividual(idReport);
    }

    @Transactional(readOnly = true)
    public Page<ReportReceitaOutput> listarMeusReportsPaginado(Long userId, int page, int size, boolean ativo) {
        return denunciaRepository.findMeusReports(PageRequest.of(page, size), ativo, userId);
    }
}