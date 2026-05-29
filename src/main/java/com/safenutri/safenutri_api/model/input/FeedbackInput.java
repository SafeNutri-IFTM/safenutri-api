package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FeedbackInput {
    @NotBlank(message = "A descrição do feedback não pode ser vazia")
    private String descricao;

}