package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_role")
    @SequenceGenerator(sequenceName = "id_seq_role", allocationSize = 1, name = "id_seq_role")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "role", nullable = false)
    private String role;
}