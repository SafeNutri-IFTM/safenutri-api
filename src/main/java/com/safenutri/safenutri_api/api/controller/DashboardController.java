package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.DashboardService;
import com.safenutri.safenutri_api.core.security.JwtService;
import com.safenutri.safenutri_api.core.security.UserPrincipal;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final JwtService jwtService;

    @GetMapping
    @Operation(summary = "Busca as receitas que atendem as restrições do usuário autenticado", description = "Busca as receitas que atendam as restrições do usuário autenticado, dando prioridade para as receitas do nutricionista do usuário, caso ele seja um paciente")
    public ResponseEntity<Page<ReceitaCardOutput>> getReceitas(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Page<ReceitaCardOutput> receitas = dashboardService.getReceitas(user.getId(), page, size);
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/top")
    @Operation(summary = "Busca o top 3 receitas mais bem avaliadas com base na restrição do usuário")
    public ResponseEntity<List<ReceitaCardOutput>> getTop3ReceitasAvaliadas() {
        Long userId = jwtService.getIdUsuario();
        List<ReceitaCardOutput> receitas = dashboardService.getTop3ReceitasFiltradas(userId);
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/top-geral")
    @Operation(summary = "Busca o top 3 receitas mais bem avaliadas")
    public ResponseEntity<List<ReceitaCardOutput>> getTop3ReceitasAvaliadasGeral() {
        List<ReceitaCardOutput> receitas = dashboardService.getTop3GeralReceitas();
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

}
