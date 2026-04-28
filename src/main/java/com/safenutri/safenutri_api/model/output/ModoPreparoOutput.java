package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModoPreparoOutput {
    private Long passo;
    private String titulo;
    private String descricao;
}
