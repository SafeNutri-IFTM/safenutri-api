package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceitaCardOutput {
    private Long id;
    private String tipoReceita;
    private String titulo;
    private String descricao;
    private Integer tempo;
    private Integer calorias;
    private Double mediaAvaliacao;
    private Boolean favoritada;
    private Boolean activated;
}
