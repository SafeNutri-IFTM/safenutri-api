package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "restricao_alimentar")
@Getter
@Setter
public class RestricaoAlimentar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição da restrição é obrigatória")
    @Column(nullable = false)
    private String restricao;
}
