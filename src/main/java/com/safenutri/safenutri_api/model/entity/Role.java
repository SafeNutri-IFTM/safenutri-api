package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_role")
    @SequenceGenerator(sequenceName = "id_seq_role", allocationSize = 1, name = "id_seq_role")
    private Long id;

    @Column(name = "role", nullable = false)
    private String role;
}