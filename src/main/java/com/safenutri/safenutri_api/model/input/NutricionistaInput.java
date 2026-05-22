package com.safenutri.safenutri_api.model.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NutricionistaInput {
    @NotNull
    @Valid
    private UserInput userInput;

    @NotBlank
    private String CRN;

    @NotBlank
    private String inscricao;
}
