package com.safenutri.safenutri_api.model.input;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor 
public class UserInput {
    @NotNull
    @Positive
    private Long genero;

    @NotNull
    @Positive
    private Long role;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    private LocalDate dtNascimento;

    @NotNull
    private List<@Positive Long> restricoes;

    private String sobreMim;
}
