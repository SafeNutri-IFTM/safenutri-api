package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "genero")
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_genero")
    @SequenceGenerator(sequenceName = "id_seq_genero", allocationSize = 1, name = "id_seq_genero")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "genero", nullable = false)
    private String genero;
}
