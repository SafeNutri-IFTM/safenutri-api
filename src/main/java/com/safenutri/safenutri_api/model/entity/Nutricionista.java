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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_nutricionista")
    @SequenceGenerator(sequenceName = "id_seq_nutricionista", allocationSize = 1, name = "id_seq_nutricionista")
    private Long id;

    @OneToOne
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
