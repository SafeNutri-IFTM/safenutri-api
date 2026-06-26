package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.core.security.jwt.UserPrincipal;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.safenutri.safenutri_api.model.output.MensagemOutput;
import com.safenutri.safenutri_api.api.service.ChatService;
import org.springframework.security.core.Authentication;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload MensagemOutput chatMessage,
            Authentication authentication
    ) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        chatMessage.setRemetenteId(user.getId());

        try {
            MensagemOutput mensagemSalva = chatService.salvar(chatMessage);

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(chatMessage.getDestinatarioId()),
                    "/queue/mensagens",
                    mensagemSalva
            );

        } catch (Exception e) {
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(user.getId()),
                    "/queue/erros",
                    e.getMessage()
            );
        }
    }
}
