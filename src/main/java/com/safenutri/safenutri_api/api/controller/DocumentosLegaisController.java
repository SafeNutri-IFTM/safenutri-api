package com.safenutri.safenutri_api.api.controller;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safenutri.safenutri_api.api.service.DocumentosLegaisService;
import com.safenutri.safenutri_api.model.entity.DocumentosLegais;

@RestController
@RequestMapping("/documentos-legais")
@RequiredArgsConstructor
public class DocumentosLegaisController {
    private final DocumentosLegaisService service;

    @GetMapping("/atual")
    @Operation(summary = "Busca o documento ativo por tipo")
    public ResponseEntity<DocumentosLegais> buscarAtual(@RequestParam String tipo) {
        return ResponseEntity.ok(service.buscarAtualPorTipo(tipo));
    }
}
