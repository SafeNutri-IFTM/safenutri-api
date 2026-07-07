package com.safenutri.safenutri_api.model.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PerfilNutricionistaPublicoOutput {
    private Long id;
    private String nome;
    private String iniciais;
    private String crn;
    private String descricao;
    private List<String> restricoes;
    private EstatisticasNutricionistaOutput estatisticas;
    private List<ReceitaCardOutput> receitas;
    private String imagem;
    private List<AtividadeRecenteOutput> atividades;
}
