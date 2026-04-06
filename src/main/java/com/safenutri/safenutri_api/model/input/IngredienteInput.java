package com.safenutri.safenutri_api.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredienteInput {
    private String ingrediente;
    private String unidade;
    private Integer quantidade;
}
