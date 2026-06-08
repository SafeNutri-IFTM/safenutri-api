package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.api.service.FeedbackService;
import com.safenutri.safenutri_api.model.input.FeedbackInput;
import com.safenutri.safenutri_api.model.output.FeedbackOutput;
import com.safenutri.safenutri_api.core.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // --- Rota do Usuário (Restaurada) ---
    @PostMapping
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

    // --- Rotas do Administrador (Novas) ---
    @GetMapping("/todos")
    public ResponseEntity<Page<FeedbackOutput>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(feedbackService.listarTodosPaginado(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackOutput> buscarPorId(@PathVariable Long id) {
        FeedbackOutput detalhe = feedbackService.buscarDetalhePorId(id);
        if (detalhe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalhe);
    }
}