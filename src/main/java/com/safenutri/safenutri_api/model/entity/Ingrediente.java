package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ingrediente")
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_ingrediente")
    @SequenceGenerator(sequenceName = "id_seq_ingrediente", allocationSize = 1, name = "id_seq_ingrediente")
    private Long id;

    @Column(name = "ingrediente", nullable = false)
    private String ingrediente;
}
