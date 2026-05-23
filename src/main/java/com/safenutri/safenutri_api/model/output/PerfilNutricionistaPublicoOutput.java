package com.safenutri.safenutri_api.model.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilNutricionistaPublicoOutput {
    private Long id;
    private String nome;
    private String iniciais;
    private String crn;
    private String descricao;
    private EstatisticasNutricionistaOutput estatisticas;
    private List<ReceitaCardOutput> receitas;
}
