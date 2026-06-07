package com.safenutri.safenutri_api.model.output;
import java.time.LocalDateTime;

public record ReportReceitaDetalheOutput(
    Long id,
    LocalDateTime data,
    String nomeAutorReport,
    String nomeReceita,
    String nomeAutorReceita,
    String motivo,
    String descricao
) {}