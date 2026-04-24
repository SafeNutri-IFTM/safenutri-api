package com.safenutri.safenutri_api.api.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import lombok.var;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;

import com.safenutri.safenutri_api.api.service.EmailSenderService;
import com.safenutri.safenutri_api.api.service.JwtService;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.output.EmailOutput;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailSenderService emailSenderService;
    private final JwtService jwtService;

    @Value("${urlAtivacao}")
    String uriAtivacao;

    @Value("${urlRecuperacao}")
    String uriRecuperacao;

    private String loadEmailTemplate(String templateName) throws IOException {
        var resource = new org.springframework.core.io.ClassPathResource("templates/" + templateName);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return template;
    }

    public ResponseEntity<?> enviaEmail(EmailOutput dados) throws MessagingException {
        this.emailSenderService.sendEmail(dados.getEmailUsuario(), dados.getAssunto(), dados.getMensagem());
        return ResponseEntity.ok("Success");
    }

    EmailOutput createEmailUser(User createdUser) {
        String jwt = jwtService.gerarTokenEmail(createdUser);
        String mensagem = "<body style=\"margin: 0; padding: 0;\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" bgcolor=\"#eeeeee\">" +
                "<tr>" +
                "<td style=\"padding: 32px 0;\">" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\">"
                +
                "<tr>" +
                "<td align=\"center\" bgcolor=\"#12295d\" style=\"padding: 32px 32px; color: #ffffff; border-collapse: collapse; font-size: 28px; font-weight: bold; font-family: Arial, sans-serif;\">"
                +
                "<b>PROCESSO SELETIVO</b>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td bgcolor=\"#ffffff\" style=\"padding: 32px 32px;\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "<tr>" +
                "<td align=\"center\" style=\"color: #12295d; font-family: Arial, sans-serif; font-size: 24px;\">" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 0 0 0 0; color: #12295d; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">"
                +
                "<p>Prezado(a) candidato(a), " + createdUser.getNome() + " </p>" +
                "<p>Clique no link abaixo para ativar a sua conta.</p>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td bgcolor=\"#12295d\" style=\"padding: 32px 32px;\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "<tr>" +
                "<td style=\"font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">" +
                "<a href=\"" + this.uriAtivacao + jwt + "\"><font color=\"#ffffff\">" + this.uriAtivacao + jwt
                + "</font></a>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td bgcolor=\"#ffffff\" style=\"padding: 32px 32px;\">" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "<tr>" +
                "<td style=\"padding: 0 0; color: #12295d; font-family: Arial, sans-serif; font-size: 14px; line-height: 20px;\">"
                +
                "Por favor, não responda este e-mail. Para maiores informações, entre em contato com a CODIUB pelo email codiub@codiub.com.br, ou pelo telefone (34)-3319-6900."
                +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</body>";

        EmailOutput emailOutput = new EmailOutput();
        emailOutput.setEmailUsuario(createdUser.getEmail());
        emailOutput.setAssunto("Ativação de conta");
        emailOutput.setMensagem(mensagem);
        return emailOutput;
    }

    EmailOutput createEmailRecuperarSenha(Optional<User> user) throws IOException {
        String template = loadEmailTemplate("email_reset_password.html");
        String jwt = jwtService.gerarTokenEmail(user.get());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("username", user.get().getNome());
        placeholders.put("resetPasswordLink", uriRecuperacao + jwt);

        String mensagem = replacePlaceholders(template, placeholders);

        EmailOutput emailOutput = new EmailOutput();
        emailOutput.setEmailUsuario(user.get().getEmail());
        emailOutput.setAssunto("Recuperação de senha");
        emailOutput.setMensagem(mensagem);
        return emailOutput;
    }

}
