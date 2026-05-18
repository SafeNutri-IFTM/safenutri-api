package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "status_denuncia")
public class StatusDenuncia {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_status_denuncia")
    @SequenceGenerator(name = "id_seq_status_denuncia", sequenceName = "id_seq_status_denuncia", allocationSize = 1)
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;
}
