package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceitaPieChartOutput {
    private String tipoRestricao;
    private Long qntd;
}
