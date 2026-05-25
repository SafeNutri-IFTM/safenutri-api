package com.safenutri.safenutri_api.model.entity;

import java.time.LocalDateTime;
import com.safenutri.safenutri_api.model.defaults.DefaultEntity;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "restricao_pessoal")
public class RestricaoPessoal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_restricao_pessoal")
    @SequenceGenerator(sequenceName = "id_seq_restricao_pessoal", allocationSize = 1, name = "id_seq_restricao_pessoal")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "restricao", nullable = false)
    private String restricao;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated = LocalDateTime.now();
    }

    public RestricaoPessoal(User user, String restricao, String descricao) {
        this.user = user;
        this.restricao = restricao;
        this.descricao = descricao;
    }
}
