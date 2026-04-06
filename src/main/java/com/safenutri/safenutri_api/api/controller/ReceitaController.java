package com.safenutri.safenutri_api.api.controller;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.ReceitaService;
import com.safenutri.safenutri_api.model.input.ReceitaInput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receita")
public class ReceitaController {
    private final ReceitaService receitaService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Cria uma receita", description = "Cria uma nova receita com base nas informações fornecidas")
    public ResponseEntity<String> save(@ModelAttribute ReceitaInput receitaInput) {
        try {
            receitaService.save(receitaInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erro ao salvar Receita: " + e.getMessage() + "\"}");
        }
    }
    
}
