package com.safenutri.safenutri_api.model.entity;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "paciente")
public class Paciente extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_paciente")
    @SequenceGenerator(sequenceName = "id_seq_paciente", allocationSize = 1, name = "id_seq_paciente")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "nutricionista_id", nullable = false)
    private User nutricionista;

}
