package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
@Getter
@Setter
public class ReceitaCardOutput {
    private Long id;
    private String tipoReceita;
    private String titulo;
    private String descricao;
    private Integer tempo;
    private String tempoFormatado;
    private Integer calorias;
    private Double mediaAvaliacao;
    private Boolean favoritada;
    private Boolean activated;

    public ReceitaCardOutput(Long id, String tipoReceita, String titulo, String descricao, Integer tempo,
        Integer calorias, Double mediaAvaliacao, Boolean favoritada, Boolean activated
    ) {
        this.id = id;
        this.tipoReceita = tipoReceita;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tempo = tempo;
        this.calorias = calorias;
        this.mediaAvaliacao = mediaAvaliacao;
        this.favoritada = favoritada;
        this.activated = activated;
    }
}
