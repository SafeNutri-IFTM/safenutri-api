package com.safenutri.safenutri_api.model.output;

import java.time.LocalDateTime;

public record FeedbackOutput(
    Long id,
    String nomeUsuario,
    String descricao,
    LocalDateTime data
) {}