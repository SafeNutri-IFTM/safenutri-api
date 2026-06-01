package com.safenutri.safenutri_api.model.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReceitaDetalheOutput {
    private ReceitaOutput receita;
    private List<ModoPreparoOutput> passos;
    private List<RestricaoOutput> restricoes;
}