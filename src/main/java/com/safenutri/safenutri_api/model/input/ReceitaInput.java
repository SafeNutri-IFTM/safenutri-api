package com.safenutri.safenutri_api.model.input;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
public class ReceitaInput {
    @NotNull
    @Positive
    private Long tipoReceitaId;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    @NotBlank
    private String tempoPreparo;

    @NotNull
    @Positive
    private Integer porcao;

    @NotNull
    @Positive
    private Integer calorias;

    private MultipartFile imagem;

    @NotEmpty
    private List<@Positive Long> restricoes;

    @NotBlank
    private String ingredientes;

    @NotEmpty
    @Valid
    private List<ModoPreparoInput> passos;
}
