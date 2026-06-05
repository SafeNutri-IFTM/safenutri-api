package com.safenutri.safenutri_api.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.DocumentosLegaisService;
import com.safenutri.safenutri_api.model.input.DocumentosLegaisInput;
import com.safenutri.safenutri_api.model.output.DocumentosLegaisOutput;

@RestController
@RequestMapping("/documentos-legais")
@RequiredArgsConstructor
public class DocumentosLegaisController {

    private final DocumentosLegaisService service;

    @GetMapping("/atual")
    @Operation(summary = "Busca o documento ativo por tipo")
    public ResponseEntity<DocumentosLegaisOutput> buscarAtual(@RequestParam String tipo) {
        return ResponseEntity.ok(service.buscarAtualPorTipo(tipo));
    }

    @PostMapping
    @Operation(summary = "Cria uma nova versão de um documento legal")
    public ResponseEntity<DocumentosLegaisOutput> criarNovaVersao(@Valid @RequestBody DocumentosLegaisInput dto) {
        DocumentosLegaisOutput novoDocumento = service.criarNovaVersao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDocumento);
    }
}