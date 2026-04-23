package com.safenutri.safenutri_api.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "modo_preparo")
public class ModoPreparo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_modo_preparo")
    @SequenceGenerator(sequenceName = "id_seq_modo_preparo", allocationSize = 1, name = "id_seq_modo_preparo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;

    @Column(name = "passo", nullable = false)
    private Integer passo;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "created")
    private LocalDateTime created;

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now();
    }

    public ModoPreparo(Receita receita, Integer passo, String titulo, String descricao) {
        this.receita = receita;
        this.passo = passo;
        this.titulo = titulo;
        this.descricao = descricao;
    }
}
