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
public class UserListOutput {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dtNascimento;
    private LocalDateTime created;
}