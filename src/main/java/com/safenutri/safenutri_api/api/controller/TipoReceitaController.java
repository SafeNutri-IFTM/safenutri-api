package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.TipoReceitaService;
import com.safenutri.safenutri_api.model.entity.TipoReceita;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tipo-receita")
public class TipoReceitaController {
    private final TipoReceitaService tipoReceitaService;

    @GetMapping
    @Operation(summary = "Busca todos os tipos de receitas", description = "Busca todos os tipos de receitas cadastrados")
    public ResponseEntity<List<TipoReceita>> getAll() {
        List<TipoReceita> tiposReceitas = tipoReceitaService.getAll();
        return ResponseEntity.ok(tiposReceitas);
    }
}
