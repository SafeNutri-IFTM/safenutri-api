package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "receita")
@Getter
@Setter
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String tempoPreparo;
    private String porcao;
    private String calorias;
    private String foto;

    @Column(name = "dt_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Usuario autor;

    // Você precisará criar a entidade TipoReceita para este relacionamento
    @ManyToOne
    @JoinColumn(name = "id_tipo_receita")
    private TipoReceita tipoReceita;
}