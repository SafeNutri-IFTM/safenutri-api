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
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_feedback")
    @SequenceGenerator(name = "id_seq_feedback", sequenceName = "id_seq_feedback", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "created")
    private LocalDateTime created;

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now();
    }

    public Feedback(User user, String descricao) {
        this.user = user;
        this.descricao = descricao;
    }

}
