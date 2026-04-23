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
@Table(name = "user_restricao")
public class UserRestricao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_user_restricao")
    @SequenceGenerator(sequenceName = "id_seq_user_restricao", allocationSize = 1, name = "id_seq_user_restricao")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restricao_id")
    private RestricaoAlimentar restricao; 

    @Column(name = "created")
    private LocalDateTime created;

    public UserRestricao(User user, RestricaoAlimentar restricao) {
        this.user = user;
        this.restricao = restricao;
    }

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now();
    }
}
