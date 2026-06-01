package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "genero")
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_genero")
    @SequenceGenerator(sequenceName = "id_seq_genero", allocationSize = 1, name = "id_seq_genero")
    private Long id;


    @Column(name = "genero", nullable = false)
    private String genero;
}
