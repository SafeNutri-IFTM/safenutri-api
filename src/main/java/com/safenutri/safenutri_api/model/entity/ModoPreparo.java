package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

@Entity
@Getter
@Setter
@Table(name = "modo_preparo")
public class ModoPreparo extends DefaultEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_modo_preparo")
    @SequenceGenerator(sequenceName = "id_seq_modo_preparo", allocationSize = 1, name = "id_seq_modo_preparo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;

    @Column(name = "passo", nullable = false)
    private Integer passo;

    @Column(name = "descricao", nullable = false)
    private String descricao;
}
