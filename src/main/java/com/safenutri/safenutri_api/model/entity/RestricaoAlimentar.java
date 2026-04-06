package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "restricao_alimentar")
public class RestricaoAlimentar extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_restricao_alimentar")
    @SequenceGenerator(sequenceName = "id_seq_restricao_alimentar", allocationSize = 1, name = "id_seq_restricao_alimentar")
    private Long id;

    @Column(name = "restricao", nullable = false)
    private String restricao;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "approved")
    private Boolean approved;

    public RestricaoAlimentar(String restricao, String descricao) {
        this.restricao = restricao;
        this.descricao = descricao;
    }

    @PrePersist
    public void onCreate() {
        this.approved = Boolean.FALSE;
    }
}
