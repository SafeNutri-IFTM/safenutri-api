package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class AvaliacaoInput {
    @NotNull
    @Positive
    private Integer nota;

    private String comentario;
}