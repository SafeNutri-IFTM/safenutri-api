package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contato_nutricionista")
@Getter
@Setter
public class ContatoNutricionista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_nutri")
    private Nutricionista nutricionista;

    @ManyToOne
    @JoinColumn(name = "id_tipo_cntt")
    private TipoContato tipoContato;

    @Column(nullable = false)
    private String contato; // O valor real (ex: "@nutri_alberto" ou "3499999999")
}
