package com.safenutri.safenutri_api.model.output;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AtividadeRecenteOutput {
    String tipoAtividade;
    Long receitaId;
    String receitaTitulo;
    Long qntdEstrela;
    LocalDateTime updated;
}
