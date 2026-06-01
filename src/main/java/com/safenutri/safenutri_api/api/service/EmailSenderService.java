package com.safenutri.safenutri_api.api.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.safenutri.safenutri_api.core.security.JwtService;
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
        var helper = new MimeMessageHelper(mensagem, true, "UTF-8");

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