package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipo_contato")
@Getter
@Setter
public class TipoContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // Exemplo: "WhatsApp", "Instagram", "E-mail"
}