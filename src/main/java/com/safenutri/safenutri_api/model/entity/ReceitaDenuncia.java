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
@Table(name = "receita_denuncia")
public class ReceitaDenuncia extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_receita_denuncia")
    @SequenceGenerator(sequenceName = "id_seq_receita_denuncia", allocationSize = 1, name = "id_seq_receita_denuncia")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusDenuncia status;

    public ReceitaDenuncia(User user, Receita receita, String descricao, StatusDenuncia status) {
        this.user = user;
        this.receita = receita;
        this.descricao = descricao;
        this.status = status;
    }
}
