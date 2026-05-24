package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor // O Jackson exige isso para transformar o JSON recebido no objeto
@AllArgsConstructor
public class FeedbackInput {
    
    @NotBlank(message = "A descrição do feedback não pode ser vazia")
    private String descricao;

}