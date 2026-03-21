package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nutricionista")
@Getter
@Setter
public class Nutricionista {

    @Id
    private Long id_user;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_user")
    private Usuario usuario;

    private String crn;

    @Column(name = "desc_perfil")
    private String descricaoPerfil;

    @Column(name = "foto_perfil")
    private String fotoPerfil;
}
