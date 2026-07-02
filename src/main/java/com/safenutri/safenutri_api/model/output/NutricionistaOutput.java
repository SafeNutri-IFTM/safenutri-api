package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Getter
@Setter
public class NutricionistaOutput {
    private String nome;
    private String email;
    private Long genero;
    private LocalDate dtNascimento;
    private LocalDateTime created;
    private Long qntdFavoritas;
    private Long qntdAvaliadas;
    private Long qntdMinhasReceitas;
    private Long paciente;
    private Long diasConta;
    private String descricao;
    private String crn;
    private String imagem;

    public NutricionistaOutput(String nome, String email, Long genero, LocalDate dtNascimento, LocalDateTime created,
                      Long qntdFavoritas, Long qntdAvaliadas, Long qntdMinhasReceitas, Long paciente, String descricao, String crn, String imagem) {
        this.nome = nome;
        this.email = email;
        this.genero = genero;
        this.dtNascimento = dtNascimento;
        this.created = created;
        this.qntdFavoritas = qntdFavoritas;
        this.qntdAvaliadas = qntdAvaliadas;
        this.qntdMinhasReceitas = qntdMinhasReceitas;
        this.paciente = paciente;
        this.descricao = descricao;
        this.crn = crn;
        this.imagem = imagem;
    }
}
