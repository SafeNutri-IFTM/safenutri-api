package com.safenutri.safenutri_api.model.output;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailOutput {
    @NotBlank
    String emailUsuario;
    @NotBlank
    String assunto;
    @NotBlank
    String mensagem;
}
