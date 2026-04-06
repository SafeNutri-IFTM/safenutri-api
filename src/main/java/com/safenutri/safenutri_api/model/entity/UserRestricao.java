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
@Table(name = "user_restricao")
public class UserRestricao extends DefaultEntity {
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

    public UserRestricao(User user, RestricaoAlimentar restricao) {
        this.user = user;
        this.restricao = restricao;
    }
}
