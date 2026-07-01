package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safenutri.safenutri_api.api.service.ChatService;
import com.safenutri.safenutri_api.core.security.jwt.UserPrincipal;
import com.safenutri.safenutri_api.model.output.ContatoOutput;
import com.safenutri.safenutri_api.model.output.MensagemOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRestController {
    private final ChatService chatService;

    @GetMapping("/contatos")
    @Operation(summary = "Retorna os contatos do usuário autenticado")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ContatoOutput>> listarContatos(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        List<ContatoOutput> contatos = chatService.buscarContatosComUltimaMensagem(user.getId());
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/mensagens/{contatoId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Retorna as mensagens que o usuário autenticado tem com um contato específico")
    public ResponseEntity<List<MensagemOutput>> listarMensagens(
            @PathVariable Long contatoId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        List<MensagemOutput> historico = chatService.buscarHistorico(user.getId(), contatoId);
        return ResponseEntity.ok(historico);
    }
}
