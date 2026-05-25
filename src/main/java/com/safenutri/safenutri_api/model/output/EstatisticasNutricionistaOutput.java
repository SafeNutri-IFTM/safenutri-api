package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EstatisticasNutricionistaOutput {
    private Long favoritadas;
    private Long avaliadas;
    private Long pacientes;
    private Long diasNoApp;
}
