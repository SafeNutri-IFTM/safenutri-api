package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ModoPreparoInput {
    private Integer passo;

    @NotBlank
    private String tituloEtapa;

    @NotBlank
    private String descricaoEtapa;
}
