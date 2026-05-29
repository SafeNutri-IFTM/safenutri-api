package com.safenutri.safenutri_api.api.controller;

import java.security.Principal;
import java.util.Map;

import com.safenutri.safenutri_api.core.security.UserPrincipal;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.FeedbackService;
import com.safenutri.safenutri_api.model.input.FeedbackInput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Envia um feedback", description = "Salva o feedback vinculado ao usuário logado")
    public ResponseEntity<?> enviarFeedback(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid FeedbackInput input
    ) {
        try {
            feedbackService.salvar(input.getDescricao(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of("message", "Feedback enviado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao enviar feedback: " + e.getMessage()));
        }
    }
}