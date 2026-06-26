package com.safenutri.safenutri_api.model.entity;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "nutricionista")
public class Nutricionista extends DefaultEntity {
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

    @Column(name = "imagem_perfil")
    private String imagem;

    @Column(name = "token_convite")
    private UUID tokenConvite;

    public Nutricionista(User user, String CRN) {
        this.user = user;
        this.CRN = CRN;
    }
}
