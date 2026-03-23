package com.safenutri.safenutri_api.core.utils;

import lombok.Getter;

@Getter
public class CrnInfo {
    private String CRN;
    private String nome;
    private String situacao;

    public CrnInfo(String CRN, String nome, String situacao) {
        this.CRN = CRN;
        this.nome = nome;
        this.situacao = situacao;
    }

    @Override
    public String toString() {
        return nome + " | " + CRN + " | " + situacao;
    }
}
