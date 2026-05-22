package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReceitaOutput {
    private Long id;
    private String titulo;
    private String descricao;
    private Long idTipoReceita;
    private String tipoReceita;
    private Integer porcao;
    private Integer tempoPreparo;
    private String ingredientes;
    private Integer calorias;
}
