package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "nutricionista")
public class Nutricionista {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "crn")
    private String CRN;

    @Column(name = "desc_perfil")
    private String descPerfil;

    @Column(name = "caminho_foto")
    private String caminhoFoto;

    public Nutricionista(User user, String CRN) {
        this.user = user;
        this.CRN = CRN;
    }
}
