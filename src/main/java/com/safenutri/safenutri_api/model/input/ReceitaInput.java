package com.safenutri.safenutri_api.model.input;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ReceitaInput {
    private Long tipoReceitaId;
    private String titulo;
    private String descricao;
    private Integer tempoPreparo;
    private Integer porcao;
    private Integer calorias;
    private MultipartFile imagem;
    private List<Long> restricoes;
}
