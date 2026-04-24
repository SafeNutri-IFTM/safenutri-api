package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.DashboardService;
import com.safenutri.safenutri_api.api.service.JwtService;
import com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput;

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
    public ResponseEntity<Page<ReceitaDashboardOutput>> getReceitas(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size
    ) {
        Long userId = jwtService.getIdUsuario();
        Page<ReceitaDashboardOutput> receitas = dashboardService.getReceitas(page, size, userId);
        return ResponseEntity.ok(receitas); 
    }

    @GetMapping("/top")
    @Operation(summary = "Lista o top 3 receitas mais bem avaliadas com base na restrição do usuário")
    public ResponseEntity<List<ReceitaDashboardOutput>> getTop3ReceitasAvaliadas() {
        Long userId = jwtService.getIdUsuario();
        List<ReceitaDashboardOutput> receitas = dashboardService.getTop3Receitas(userId);
        return ResponseEntity.ok(receitas);
    }
    
}
