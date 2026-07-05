package com.safenutri.safenutri_api.api.controller;

import java.util.Map;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.FeedbackService;
import com.safenutri.safenutri_api.model.input.FeedbackInput;
import com.safenutri.safenutri_api.model.output.FeedbackOutput;

import com.safenutri.safenutri_api.core.security.jwt.UserPrincipal;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/{id}")
    @Operation(summary = "Retorna informações sobre um feedback específico")
    public ResponseEntity<FeedbackOutput> buscarPorId(@PathVariable Long id) {
        FeedbackOutput detalhe = feedbackService.buscarDetalhePorId(id);
        if (detalhe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalhe);
    }

    @GetMapping("/todos")
    @Operation(summary = "Retorna todos os feedbacks cadastrados no sistema")
    public ResponseEntity<Page<FeedbackOutput>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(feedbackService.listarTodosPaginado(page, size));
    }

    @PostMapping
    @Operation(summary = "Cria um feedback")
    public ResponseEntity<?> enviarFeedback(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid FeedbackInput input) {
        try {
            feedbackService.salvar(input.getDescricao(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Feedback enviado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao enviar feedback: " + e.getMessage()));
        }
    }

}