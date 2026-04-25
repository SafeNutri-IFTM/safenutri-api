package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.safenutri.safenutri_api.api.service.ReceitaService;
import com.safenutri.safenutri_api.api.service.UploadService;
import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.model.input.ReceitaInput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receita")
public class ReceitaController {
    private final ReceitaService receitaService;
    private final UploadService uploadService;

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

    @GetMapping("/download-image/{id}")
    @Operation(summary = "Busca a imagem pelo ID da receita")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        try {
            Receita receita = receitaService.findById(id);

            if (receita.getCaminhoFoto() == null || receita.getCaminhoFoto().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Receita não possui imagem cadastrada\"}");
            }

            Resource resource = uploadService.carregarImagem(receita.getCaminhoFoto());

            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) 
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                .body(resource);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"error\": \"Acesso negado à imagem\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Imagem não encontrada no servidor\"}");
        }
    }
    
}
