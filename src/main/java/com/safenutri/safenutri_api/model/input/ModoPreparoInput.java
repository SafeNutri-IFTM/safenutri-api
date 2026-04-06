package com.safenutri.safenutri_api.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModoPreparoInput {
    private Integer passo;
    private String tituloEtapa;
    private String descricaoEtapa;
}
