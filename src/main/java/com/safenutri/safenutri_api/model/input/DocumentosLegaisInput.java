package com.safenutri.safenutri_api.model.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentosLegaisInput {
    @NotBlank
    private String tipo; // Ex: "TERMO_USO_COMUM"

    @NotBlank
    private String texto; // O novo texto digitado pelo Admin
}
