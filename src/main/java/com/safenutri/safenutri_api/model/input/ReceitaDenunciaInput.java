package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaDenunciaInput {

    @NotBlank(message = "O motivo da denúncia não pode estar em branco")
    private String motivo;
}