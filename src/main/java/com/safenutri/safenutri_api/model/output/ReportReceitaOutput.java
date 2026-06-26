package com.safenutri.safenutri_api.model.output;
import java.time.LocalDateTime;

public record ReportReceitaOutput(
    Long id,
    Long receitaId,
    String nomeReceita,
    String nomeAutorReport,
    LocalDateTime data,
    String motivo,
    String status
) {}