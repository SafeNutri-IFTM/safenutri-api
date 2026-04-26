package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceitaDashboardOutput {
    private Long id;
    private String tipoReceita;
    private String titulo;
    private Integer tempo;
    private Integer calorias;
    private Double mediaAvaliacao;
}
