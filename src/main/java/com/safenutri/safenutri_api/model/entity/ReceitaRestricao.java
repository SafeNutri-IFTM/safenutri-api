package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

@Entity
@Getter
@Setter
@Table(name = "receita_restricao")
public class ReceitaRestricao extends DefaultEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_receita_restricao")
    @SequenceGenerator(sequenceName = "id_seq_receita_restricao", allocationSize = 1, name = "id_seq_receita_restricao")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;

    @ManyToOne
    @JoinColumn(name = "restricao_id", nullable = false)
    private RestricaoAlimentar restricao;
}
