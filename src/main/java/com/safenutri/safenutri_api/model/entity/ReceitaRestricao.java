package com.safenutri.safenutri_api.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "receita_restricao")
public class ReceitaRestricao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_receita_restricao")
    @SequenceGenerator(sequenceName = "id_seq_receita_restricao", allocationSize = 1, name = "id_seq_receita_restricao")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;

    @ManyToOne
    @JoinColumn(name = "restricao_id", nullable = false)
    private RestricaoAlimentar restricao;

    @Column(name = "created")
    private LocalDateTime created;

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now();
    }

    public ReceitaRestricao(Receita receita, RestricaoAlimentar restricao) {
        this.receita = receita;
        this.restricao = restricao;
    }
}
