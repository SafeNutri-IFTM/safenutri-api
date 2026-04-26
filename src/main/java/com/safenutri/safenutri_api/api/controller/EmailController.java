package com.safenutri.safenutri_api.api.controller;

import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safenutri.safenutri_api.api.service.EmailSenderService;
import com.safenutri.safenutri_api.model.output.EmailOutput;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailSenderService emailSenderService;

    @PostMapping("/enviar")
    public ResponseEntity<?> enviaEmail(@RequestBody EmailOutput dados) throws MessagingException {
        this.emailSenderService.sendEmail(dados.getEmailUsuario(), dados.getAssunto(), dados.getMensagem());
        return ResponseEntity.ok("E-mail enviado com sucesso");
    }
}