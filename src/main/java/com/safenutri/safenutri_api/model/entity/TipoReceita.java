package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tipo_receita")
public class TipoReceita {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_tipo_receita")
    @SequenceGenerator(sequenceName = "id_seq_tipo_receita", allocationSize = 1, name = "id_seq_tipo_receita")
    private Long id;

    @Column(name = "tipo_receita", nullable = false)
    private String tipoReceita;
}
