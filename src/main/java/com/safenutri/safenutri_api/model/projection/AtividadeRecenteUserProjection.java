package com.safenutri.safenutri_api.model.projection;

import java.time.LocalDateTime;

public interface AtividadeRecenteUserProjection {
    String getTipoAtividade();
    Long getReceitaId();
    String getReceitaTitulo();
    Long getEstrela();
    LocalDateTime getDataAtividade();
}
