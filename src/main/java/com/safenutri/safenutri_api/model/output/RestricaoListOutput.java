package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RestricaoListOutput {
    private Long id;
    private String restricao;
    private String descricao;
}
