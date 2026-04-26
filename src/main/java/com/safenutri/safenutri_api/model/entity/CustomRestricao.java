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
@Table(name = "custom_restricao")
public class CustomRestricao extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_custom_restricao")
    @SequenceGenerator(sequenceName = "id_seq_custom_restricao", allocationSize = 1, name = "id_seq_custom_restricao")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "restricao", nullable = false)
    private String restricao;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "public")
    private Boolean publicBoolean;

    public CustomRestricao(User user, String restricao, String descricao, Boolean publicBoolean) {
        this.user = user;
        this.restricao = restricao;
        this.descricao = descricao;
        this.publicBoolean = publicBoolean;
    }
}
