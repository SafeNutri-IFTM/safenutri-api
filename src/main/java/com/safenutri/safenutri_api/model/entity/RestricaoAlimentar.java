package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

@Entity
@Getter
@Setter
@Table(name = "restricao_alimentar")
public class RestricaoAlimentar extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_restricao_alimentar")
    @SequenceGenerator(sequenceName = "id_seq_restricao_alimentar", allocationSize = 1, name = "id_seq_restricao_alimentar")
    private Long id;

    @Column(name = "restricao", nullable = false)
    private String restricao;
}
