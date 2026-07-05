package com.safenutri.safenutri_api.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.ReportReceitaService;
import com.safenutri.safenutri_api.core.security.jwt.UserPrincipal;
import com.safenutri.safenutri_api.model.output.ReportReceitaOutput;

import com.safenutri.safenutri_api.model.output.ReportReceitaDetalheOutput;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/report-receita")
public class ReportReceitaController {
    private final ReportReceitaService reportService;

    @GetMapping("/todos")
    @Operation(summary = "Retorna todos as denúncias cadastradas no sistema")
    public ResponseEntity<Page<ReportReceitaOutput>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean ativo) { 
        return ResponseEntity.ok(reportService.listarTodosPaginado(page, size, ativo));
    }

    @GetMapping("/meus")
    @Operation(summary = "Retorna todos as denúncias cadastradas do usuário autenticado")
    public ResponseEntity<Page<ReportReceitaOutput>> listarMeusReports(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean ativo) { 
        return ResponseEntity.ok(reportService.listarMeusReportsPaginado(user.getId(), page, size, ativo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna informações sobre uma denúncia específica")
    public ResponseEntity<ReportReceitaDetalheOutput> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.buscarDetalhePorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma denúncia específica")
    public ResponseEntity<Void> excluirReport(@PathVariable Long id) {
        reportService.excluirReport(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/inativar")
    @Operation(summary = "Inativa a receita denunciada")
    public ResponseEntity<Void> inativarReceita(@PathVariable Long id) {
        reportService.inativarReceitaPorReport(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/aprovar")
    @Operation(summary = "Aprova correção feita pelo nutricionista sobre a receita denunciada")
    public ResponseEntity<Void> aprovarReport(@PathVariable Long id) {
        reportService.aprovarReport(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/recusar")
    @Operation(summary = "Recusa a correção feita pelo nutricionista sobre a receita denunciada")
    public ResponseEntity<Void> recusarReport(@PathVariable Long id) {
        reportService.recusarReport(id);
        return ResponseEntity.ok().build();
    }
}