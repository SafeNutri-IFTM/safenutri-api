package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "receita_ingrediente")
public class ReceitaIngrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_receita_ingrediente")
    @SequenceGenerator(sequenceName = "id_seq_receita_ingrediente", allocationSize = 1, name = "id_seq_receita_ingrediente")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "unidade", nullable = false)
    private String unidade;
}
