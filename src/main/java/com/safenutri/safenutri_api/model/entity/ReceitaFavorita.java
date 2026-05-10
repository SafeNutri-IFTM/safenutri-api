package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "receita_favorita", schema = "public")
@Getter 
@Setter 
@NoArgsConstructor
public class ReceitaFavorita {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favorita_generator")
    @SequenceGenerator(
        name = "favorita_generator", 
        sequenceName = "id_seq_receita_favorita", 
        schema = "public", 
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;

    @Column(name = "created", insertable = false, updatable = false)
    private LocalDateTime created;

    public ReceitaFavorita(User user, Receita receita) {
        this.user = user;
        this.receita = receita;
    }
}