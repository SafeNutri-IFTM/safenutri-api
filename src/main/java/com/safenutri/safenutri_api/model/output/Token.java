package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Token {
    private String email;
    private String token;
    private boolean termosPendentes;

    // Construtor auxiliar para facilitar o uso onde não há termos pendentes (ex:
    // cadastros novos)
    public Token(String email, String token) {
        this.email = email;
        this.token = token;
        this.termosPendentes = false;
    }
}