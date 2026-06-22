package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MensagemOutput {
    private Long id;
    private Long remetenteId;
    private Long destinatarioId;
    private String conteudo;
    private LocalDateTime dataEnvio;
}
