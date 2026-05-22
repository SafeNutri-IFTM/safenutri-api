package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ModoPreparoInput {
    @NotNull
    @Positive
    private Integer passo;

    @NotBlank
    private String tituloEtapa;

    @NotBlank
    private String descricaoEtapa;
}
