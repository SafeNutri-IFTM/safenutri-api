package com.safenutri.safenutri_api.model.output;

import java.util.List;
import com.safenutri.safenutri_api.model.entity.Receita;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceitaDetalheOutput {
    private Receita receita;
    private List<ModoPreparoOutput> passos;
    private List<ReceitaRestricaoOutput> restricoes;
}