package com.safenutri.safenutri_api.api.controller;

import java.util.Map;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaDetalheOutput;


import com.safenutri.safenutri_api.model.input.AvaliacaoInput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receita")
public class ReceitaController {
    private final ReceitaService receitaService;
    private final UploadService uploadService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Cria uma receita", description = "Cria uma nova receita com base nas informações fornecidas")
    public ResponseEntity<?> save(@RequestPart("receita") @Valid ReceitaInput receitaInput,
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

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Atualiza uma receita", description = "Atualiza uma receita com base nas informações fornecidas")
    public ResponseEntity<?> update(@RequestPart("receita") @Valid ReceitaInput receitaInput,
        @RequestPart(value = "imagem", required = false) MultipartFile imagem,
        @PathVariable Long id
    ) {

        try {
            receitaInput.setImagem(imagem);
            receitaService.update(receitaInput, id);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erro ao salvar Receita: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/minhas-receitas")
    public ResponseEntity<Page<ReceitaCardOutput>> getMinhasReceitas(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReceitaCardOutput> receita = receitaService.getMinhasReceitas(page, size);
        return ResponseEntity.ok(receita);
    }
    

    @GetMapping("/{id}")
    @Operation(summary = "Busca detalhes de uma receita", description = "Retorna as informações completas de uma receita pelo ID")
    public ResponseEntity<ReceitaDetalheOutput> buscarPorId(@PathVariable Long id) {
        try {
            ReceitaDetalheOutput receita = receitaService.findDetalheById(id);
            return ResponseEntity.ok(receita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa uma receita", description = "Realiza a desativação lógica da receita (setAtivo = false)")
    public ResponseEntity<?> desativar(@PathVariable Long id) {
        try {
            receitaService.desativar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erro ao desativar receita: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/download-image/{id}")
    @Operation(summary = "Busca a imagem pelo ID da receita")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        try {
            Receita receita = receitaService.findById(id);

            if (receita.getImagem() == null || receita.getImagem().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Receita não possui imagem cadastrada\"}");
            }

            Resource resource = uploadService.carregarImagem(receita.getImagem());

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

    @GetMapping("/receita-completa/{id}")
    @Operation(summary = "Busca os detalhes completos de uma receita pelo ID (Receita, Passos e Restrições)")
    public ResponseEntity<?> getReceitaById(@PathVariable Long id) {
        try {
            // Chama o serviço que empacota a receita, passos e restrições
            Map<String, Object> response = receitaService.getReceitaCompletaById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Receita não encontrada: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{id}/avaliar")
    @Operation(summary = "Adiciona ou atualiza a avaliação do usuário logado para uma receita")
    public ResponseEntity<?> avaliarReceita(
            @PathVariable Long id, 
            @RequestBody AvaliacaoInput input, 
            java.security.Principal principal) {
        
        try {
            String emailLogado = principal.getName();
            
            receitaService.avaliar(id, emailLogado, input.getNota(), input.getComentario());
            
            return ResponseEntity.ok().body("{\"message\": \"Avaliação salva com sucesso!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}/minha-avaliacao")
    @Operation(summary = "Busca a avaliação do usuário logado para uma receita específica")
    public ResponseEntity<?> getMinhaAvaliacao(@PathVariable Long id, java.security.Principal principal) {
        try {
            Map<String, Object> avaliacao = receitaService.getMinhaAvaliacao(id, principal.getName());
            
            if (avaliacao != null) {
                return ResponseEntity.ok(avaliacao);
            }
            return ResponseEntity.noContent().build(); // 204 se não avaliou ainda
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
}
