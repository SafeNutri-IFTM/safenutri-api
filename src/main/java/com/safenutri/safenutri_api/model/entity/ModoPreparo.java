package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "modo_preparo")
@Getter
@Setter
public class ModoPreparo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_receita")
    private Receita receita;

    @Column(name = "id_passo")
    private Integer passo;

    @Column(columnDefinition = "TEXT")
    private String descricao;
}
