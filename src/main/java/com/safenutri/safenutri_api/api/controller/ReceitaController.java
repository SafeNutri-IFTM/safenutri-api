package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.safenutri.safenutri_api.api.service.ReceitaService;
import com.safenutri.safenutri_api.model.input.ReceitaInput;
import com.safenutri.safenutri_api.model.entity.Receita;
@RestController
@RequiredArgsConstructor
@RequestMapping("/receita")
public class ReceitaController {
    private final ReceitaService receitaService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Cria uma receita", description = "Cria uma nova receita com base nas informações fornecidas")
    public ResponseEntity<String> save(@RequestPart("receita") @Valid ReceitaInput receitaInput,
        @RequestPart(value = "imagem", required = false) MultipartFile imagem) {

        try {
            receitaInput.setImagem(imagem);
            receitaService.save(receitaInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erro ao salvar Receita: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca detalhes de uma receita", description = "Retorna as informações completas de uma receita pelo ID")
    public ResponseEntity<Receita> buscarPorId(@PathVariable Long id) {
        try {
            Receita receita = receitaService.findById(id);
            return ResponseEntity.ok(receita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa uma receita", description = "Realiza a desativação lógica da receita (setAtivo = false)")
    public ResponseEntity<Object> desativar(@PathVariable Long id) {
        try {
            receitaService.desativar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erro ao desativar receita: " + e.getMessage() + "\"}");
        }
    }
}