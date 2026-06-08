package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.DashboardService;
import com.safenutri.safenutri_api.core.security.UserPrincipal;
import com.safenutri.safenutri_api.model.output.QtdUserByMesOutput;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;
import com.safenutri.safenutri_api.model.output.UserPieChartOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

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
    public ResponseEntity<List<ReceitaCardOutput>> getTop3ReceitasAvaliadas(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        List<ReceitaCardOutput> receitas = dashboardService.getTop3ReceitasFiltradas(user.getId());
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/top-geral")
    @Operation(summary = "Busca o top 3 receitas mais bem avaliadas")
    public ResponseEntity<List<ReceitaCardOutput>> getTop3ReceitasAvaliadasGeral() {
        List<ReceitaCardOutput> receitas = dashboardService.getTop3GeralReceitas();
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/qntd-pacientes")
    public ResponseEntity<Long> countQntdPacientes(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Long qntd = dashboardService.countPacientes(user.getId());
        return ResponseEntity.ok(qntd);
    }

    @GetMapping("/qntd-receitas")
    public ResponseEntity<Long> countQntdReceitas(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Long qntd = dashboardService.countReceitas(user.getId());
        return ResponseEntity.ok(qntd);
    }

    @GetMapping("/receitas-restricao")
    public ResponseEntity<List<ReceitaPieChartOutput>> countGroupByRestricao(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        List<ReceitaPieChartOutput> receitas = dashboardService.countGroupByRestricoes(user.getId());
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/qntd-receitas-gerais")
    public ResponseEntity<Long> countAllReceitas(
        @AuthenticationPrincipal UserPrincipal user
    ) {
        Long qntd = dashboardService.countAllReceitas();
        return ResponseEntity.ok(qntd);
    }

    @GetMapping("/qntd-restricoes")
    public ResponseEntity<Long> countAllRestricoes(
        @AuthenticationPrincipal UserPrincipal user
    ) {
        Long qntd = dashboardService.countAllRestricoes();
        return ResponseEntity.ok(qntd);
    }

    @GetMapping("/users")
    public ResponseEntity<Long> countAllUsers(
        @AuthenticationPrincipal UserPrincipal user
    ) {
        Long qntd = dashboardService.countAllUsers();
        return ResponseEntity.ok(qntd);
    }

    @GetMapping("/users-restricao")
    public ResponseEntity<List<UserPieChartOutput>> countUserGroupByRestricao(
        @AuthenticationPrincipal UserPrincipal user
    ) {
        List<UserPieChartOutput> users = dashboardService.countUserGroupByRestricao();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users-mes")
    public ResponseEntity<List<QtdUserByMesOutput>> countUserByMes(
        @AuthenticationPrincipal UserPrincipal user
    ) {
        List<QtdUserByMesOutput> users = dashboardService.countUserByMes(); 
        return ResponseEntity.ok(users);
    }

}
