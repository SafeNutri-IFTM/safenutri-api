package com.safenutri.safenutri_api.model.input;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor; 

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class UserInput {
    private Long id; 
    private Long genero;
    private Long role;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dtNascimento;
    private List<Long> restricoes;
}
