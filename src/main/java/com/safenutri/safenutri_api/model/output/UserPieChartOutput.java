package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserPieChartOutput {
    private String tipoRestricao;
    private Long qntd;
}
