package com.safenutri.safenutri_api.model.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor // Adicionado para facilitar a instanciação
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
    private String statusDenuncia; // Ex: 'SUSPENSA', 'EM ANÁLISE'
    private String motivoDenuncia; // Descrição do report

    // Este é o seu construtor original, ele DEVE ser mantido para não quebrar o código existente!
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