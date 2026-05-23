package com.safenutri.safenutri_api.model.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticasNutricionistaOutput {
    private Long favoritadas;
    private Long avaliadas;
    private Long pacientes;
    private Long diasNoApp;
}
