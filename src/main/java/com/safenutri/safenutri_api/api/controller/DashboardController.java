package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.DashboardService;
import com.safenutri.safenutri_api.api.service.JwtService;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final JwtService jwtService;

    @GetMapping
    @Operation(summary = "Lista as receitas que atendem as restrições do usuário logado", 
    description = "Lista todas as receitas que atendam as restrições do usuário, dando prioridade para as receitas do nutricionista do usuário, caso ele tenha um"
    )
    public ResponseEntity<Page<ReceitaCardOutput>> getReceitas(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size
    ) {
        Long userId = jwtService.getIdUsuario();
        Page<ReceitaCardOutput> receitas = dashboardService.getReceitas(userId, page, size);
        return ResponseEntity.ok(receitas); 
    }

    // ========================================================================================
    // NOVO ENDPOINT DA TASK SN-110: BUSCA DE RECEITAS COM PAGINAÇÃO
    // ========================================================================================
    @GetMapping("/busca")
    @Operation(summary = "Busca receitas pelo título (termo) com paginação")
    public ResponseEntity<Page<ReceitaCardOutput>> buscarReceitasFeed(
        @RequestParam(required = false, defaultValue = "") String termo,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        // Envia para o service com as configurações da página
        Page<ReceitaCardOutput> receitas = dashboardService.buscarReceitasPaginadas(termo, page, size);
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/top")
    @Operation(summary = "Lista o top 3 receitas mais bem avaliadas com base na restrição do usuário")
    public ResponseEntity<List<ReceitaCardOutput>> getTop3ReceitasAvaliadas() {
        Long userId = jwtService.getIdUsuario();
        List<ReceitaCardOutput> receitas = dashboardService.getTop3ReceitasFiltradas(userId);
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/qntd-pacientes")
    public ResponseEntity<Long> countQntdPacientes() {
        Long userId = jwtService.getIdUsuario();
        Long qntd = dashboardService.countPacientes(userId);
        return ResponseEntity.ok(qntd);
    }

    @GetMapping("/qntd-receitas")
    public ResponseEntity<Long> countQntdReceitas() {
        Long userId = jwtService.getIdUsuario();
        Long qntd = dashboardService.countReceitas(userId);
        return ResponseEntity.ok(qntd);
    }

    @GetMapping("/receitas-restricao")
    public ResponseEntity<List<ReceitaPieChartOutput>> countGroupByRestricao() {
        Long userId = jwtService.getIdUsuario();
        List<ReceitaPieChartOutput> receitas = dashboardService.countGroupByRestricoes(userId);
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/top-geral")
    @Operation(summary = "Lista o top 3 receitas mais bem avaliadas")
    public ResponseEntity<List<ReceitaCardOutput>> getTop3ReceitasAvaliadasGeral() {
        List<ReceitaCardOutput> receitas = dashboardService.getTop3GeralReceitas();
        return ResponseEntity.ok(receitas);
    }
}