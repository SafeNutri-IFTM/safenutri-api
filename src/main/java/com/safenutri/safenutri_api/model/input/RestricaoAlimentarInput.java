package com.safenutri.safenutri_api.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestricaoAlimentarInput {
    private String restricao;
    private String descricao;
}
