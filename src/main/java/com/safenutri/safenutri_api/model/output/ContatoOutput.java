package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ContatoOutput {
    private Long id;
    private String nome;
    private String ultimaMensagem;
    private String imagem;
}
