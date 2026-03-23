package com.safenutri.safenutri_api.model.input;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInput {
    private Long genero;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dtNascimento;
    private List<Long> restricoes;
}
