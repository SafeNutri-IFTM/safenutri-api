package com.safenutri.safenutri_api.model.output;

import lombok.Data;

@Data
public class DocumentosLegaisOutput {
    private Long id;
    private String tipo;
    private String versao;
    private String texto;
    private String dataCriacao;
}
