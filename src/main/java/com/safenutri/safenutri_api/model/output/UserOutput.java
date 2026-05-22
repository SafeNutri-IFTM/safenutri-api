package com.safenutri.safenutri_api.model.output;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
@Getter
@Setter
public class UserOutput {
    private String nome;
    private String email;
    private String genero;
    private LocalDate dtNascimento;
    private LocalDateTime created;
    private Long qntdFavoritas;
    private Long qntdAvaliadas;
    private Long qntdMinhasReceitas;
    private Long diasConta;
    private String crn;
    private String sobreMim;

    public UserOutput(String nome, String email, String genero, LocalDate dtNascimento, LocalDateTime created,
        Long qntdFavoritas, Long qntdAvaliadas, Long qntdMinhasReceitas, String crn, String sobreMim) {
            this.nome = nome;
            this.email = email;
            this.genero = genero;
            this.dtNascimento = dtNascimento;
            this.created = created;
            this.qntdFavoritas = qntdFavoritas;
            this.qntdAvaliadas = qntdAvaliadas;
            this.qntdMinhasReceitas = qntdMinhasReceitas;
            this.crn = crn;
            this.sobreMim = sobreMim;
    }
}
