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
@Table(name = "receita")
public class Receita extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_receita")
    @SequenceGenerator(sequenceName = "id_seq_receita", allocationSize = 1, name = "id_seq_receita")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tipo_receita_id", nullable = false)
    private TipoReceita tipoReceita;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "ingredientes", nullable = false)
    private String ingredientes;

    @Column(name = "tempo_preparo", nullable = false)
    private Integer tempoPreparo;

    @Column(name = "porcao", nullable = false)
    private Integer porcao;

    @Column(name = "calorias", nullable = false)
    private Integer calorias;

    @Column(name = "caminho_foto")
    private String caminhoFoto;

    @Column(name = "public")
    private Boolean publicBoolean;

    public Receita(User user, TipoReceita tipoReceita, String titulo, String descricao, String ingredientes, 
        Integer tempoPreparo, Integer porcao, Integer calorias, String caminhoFoto, Boolean publicBoolean) {
            this.user = user;
            this.tipoReceita = tipoReceita;
            this.titulo = titulo;
            this.descricao = descricao;
            this.ingredientes = ingredientes;
            this.tempoPreparo = tempoPreparo;
            this.porcao = porcao;
            this.calorias = calorias;
            this.caminhoFoto = caminhoFoto;
            this.publicBoolean = publicBoolean;
    }
}
