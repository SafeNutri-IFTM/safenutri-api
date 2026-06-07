package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.api.service.ReportReceitaService;
import com.safenutri.safenutri_api.model.output.ReportReceitaOutput;
import com.safenutri.safenutri_api.model.output.ReportReceitaDetalheOutput;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report-receita")
public class ReportReceitaController {

    private final ReportReceitaService reportService;

    public ReportReceitaController(ReportReceitaService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<ReportReceitaOutput>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean ativo) { // Recebe o boolean do filtro
        return ResponseEntity.ok(reportService.listarTodosPaginado(page, size, ativo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportReceitaDetalheOutput> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.buscarDetalhePorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirReport(@PathVariable Long id) {
        reportService.excluirReport(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/inativar")
    public ResponseEntity<Void> inativarReceita(@PathVariable Long id) {
        reportService.inativarReceitaPorReport(id);
        return ResponseEntity.ok().build();
    }
}