package com.safenutri.safenutri_api.api.service;

import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import lombok.var;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    @Value("${emailHost}")
    private String emailHost;
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String message) throws MessagingException {
        var mensagem = this.mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mensagem, true);

        helper.setFrom(emailHost);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);

        this.mailSender.send(mensagem);
    }
}
