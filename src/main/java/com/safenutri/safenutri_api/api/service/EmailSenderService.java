package com.safenutri.safenutri_api.api.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.output.EmailOutput;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Value("${emailHost}")
    private String emailHost;

    @Value("${urlAtivacao}")
    private String uriAtivacao;

    @Value("${urlRecuperacao}")
    private String uriRecuperacao;

    private final JavaMailSender mailSender;
    private final JwtService jwtService;

    // Métodos de envio de e-mail
    public void sendEmail(String to, String subject, String message) throws MessagingException {
        var mensagem = this.mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mensagem, true);

        helper.setFrom(emailHost);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);

        this.mailSender.send(mensagem);
    }

    // Lógica para carregar o arquivo HTML da pasta resources/templates
    private String loadEmailTemplate(String templateName) throws IOException {
        var resource = new org.springframework.core.io.ClassPathResource("templates/" + templateName);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    // Lógica para substituir os {{placeholders}} pelo conteúdo real
    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }

    // Cria o objeto de e-mail para Ativação de Novo Usuário
    public EmailOutput createEmailUser(User createdUser) {
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
                "Por favor, não responda este e-mail. Para mais informações, entre em contato com o suporte através do nosso site."
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

    // Cria o objeto de e-mail para Recuperação de Senha
    public EmailOutput createEmailRecuperarSenha(User user) throws IOException {
        String template = loadEmailTemplate("email_reset_password.html");
        String jwt = jwtService.gerarTokenEmail(user);

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("username", user.getNome());
        placeholders.put("resetPasswordLink", uriRecuperacao + jwt);

        String mensagem = replacePlaceholders(template, placeholders);

        EmailOutput emailOutput = new EmailOutput();
        emailOutput.setEmailUsuario(user.getEmail());
        emailOutput.setAssunto("Recuperação de senha");
        emailOutput.setMensagem(mensagem);
        return emailOutput;
    }
}