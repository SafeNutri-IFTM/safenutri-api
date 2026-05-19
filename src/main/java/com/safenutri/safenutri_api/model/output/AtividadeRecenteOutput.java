package com.safenutri.safenutri_api.model.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtividadeRecenteOutput {
    String tipoAtividade;
    Long receitaId;
    String receitaTitulo;
    Long qntdEstrela;

    LocalDateTime updated;
}
