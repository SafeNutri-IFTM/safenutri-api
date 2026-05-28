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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.safenutri.safenutri_api.core.security.UserPrincipal;
import com.safenutri.safenutri_api.api.service.ReceitaService;
import com.safenutri.safenutri_api.api.service.UploadService;
import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.model.input.ReceitaInput;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaDetalheOutput;
import com.safenutri.safenutri_api.model.input.AvaliacaoInput;
import com.safenutri.safenutri_api.model.input.ReceitaDenunciaInput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receita")
public class ReceitaController {
    private final ReceitaService receitaService;
    private final UploadService uploadService;

    @GetMapping("/minhas-receitas")
    @Operation(summary = "Busca as receitas cadastradas pelo usuário autenticado")
    public ResponseEntity<Page<ReceitaCardOutput>> getMinhasReceitas(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) Long tipoReceitaId,
            @RequestParam(required = false, defaultValue = "true") Boolean activated,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReceitaCardOutput> receita = receitaService.getMinhasReceitas(user.getId(), termo, tipoReceitaId, activated, page, size);
        return ResponseEntity.ok(receita);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca as informações de uma receita pelo ID")
    public ResponseEntity<ReceitaDetalheOutput> buscarPorId(@PathVariable Long id) {
        try {
            ReceitaDetalheOutput receita = receitaService.findDetalheById(id);
            return ResponseEntity.ok(receita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/download-image/{id}")
    @Operation(summary = "Busca a imagem da receita pelo ID")
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
            Map<String, Object> response = receitaService.getReceitaCompletaById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Receita não encontrada: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}/minha-avaliacao")
    @Operation(summary = "Busca a avaliação do usuário logado para uma receita específica")
    public ResponseEntity<?> getMinhaAvaliacao(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id
    ) {
        try {
            Map<String, Object> avaliacao = receitaService.getMinhaAvaliacao(id, user.getId());

            if (avaliacao != null) {
                return ResponseEntity.ok(avaliacao);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/favoritas")
    @Operation(summary = "Lista receitas favoritas", description = "Retorna a página de receitas que o usuário logado favoritou")
    public ResponseEntity<Page<ReceitaCardOutput>> getFavoritas(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String termo
    ) {
        Page<ReceitaCardOutput> favoritas = receitaService.getFavoritas(user.getId(), page, size, termo);
        return ResponseEntity.ok(favoritas);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Cria uma receita", description = "Cria uma nova receita com base nas informações fornecidas")
    public ResponseEntity<?> save(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestPart("receita") @Valid ReceitaInput receitaInput,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) {
        try {
            receitaInput.setImagem(imagem);
            receitaService.save(user.getId(), receitaInput);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Erro ao salvar Receita: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{id}/avaliar")
    @Operation(summary = "Adiciona ou atualiza a avaliação do usuário logado para uma receita")
    public ResponseEntity<?> avaliarReceita(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @RequestBody AvaliacaoInput input
    ) {

        try {
            receitaService.avaliar(user.getId(), id, input.getNota(), input.getComentario());

            return ResponseEntity.ok().body("{\"message\": \"Avaliação salva com sucesso!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{id}/favoritar")
    @Operation(summary = "Favorita uma receita", description = "Adiciona a receita a lista de receitas favoritas do usuário logado")
    public ResponseEntity<?> favoritar(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id
    ) {
        try {
            receitaService.favoritar(user.getId(), id);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Erro ao favoritar receita: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/{id}/denunciar")
    @Operation(summary = "Denuncia uma receita", description = "Cria uma denúncia/reporte para uma receita específica associada ao usuário autenticado")
    public ResponseEntity<?> denunciarReceita(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @RequestBody @Valid ReceitaDenunciaInput input
    ) {
        try {
            receitaService.denunciar(id, user.getId(), input.getMotivo());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Receita reportada com sucesso!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao processar denúncia"));
        }
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Atualiza uma receita", description = "Atualiza uma receita com base nas informações fornecidas")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestPart("receita") @Valid ReceitaInput receitaInput,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem,
            @PathVariable Long id
    ) {

        try {
            receitaInput.setImagem(imagem);
            receitaService.update(user.getId(), receitaInput, id);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Erro ao salvar Receita: " + e.getMessage() + "\"}");
        }
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Desativa uma receita do usuário autenticado")
    public ResponseEntity<Void> desativarReceita(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id
    ) {
        receitaService.desativarReceita(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Ativa uma receita do usuário autenticado")
    public ResponseEntity<Void> ativarReceita(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id
    ) {
        receitaService.ativarReceita(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/desfavoritar")
    @Operation(summary = "Desfavorita uma receita")
    public ResponseEntity<?> desfavoritar(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id
    ) {
        try {
            receitaService.desfavoritar(user.getId(), id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Erro ao remover receita da lista de receitas favoritas: " + e.getMessage()
                            + "\"}");
        }
    }

}