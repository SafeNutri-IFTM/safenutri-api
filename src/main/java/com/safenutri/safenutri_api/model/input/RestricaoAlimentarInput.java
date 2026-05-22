package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RestricaoAlimentarInput {
    @NotBlank
    private String restricao;

    @NotBlank
    private String descricao;
}
