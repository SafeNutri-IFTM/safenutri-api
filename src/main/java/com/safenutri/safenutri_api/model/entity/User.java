package com.safenutri.safenutri_api.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User extends DefaultEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_user")
    @SequenceGenerator(sequenceName = "id_seq_user", allocationSize = 1, name = "id_seq_user")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genero_id", nullable = false)
    private Genero genero; 

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role; 

    @NotNull
    @Size(max = 255)
    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Size(max = 50)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @NotBlank(message = "A senha é obrigatória")
    @Column(name = "senha", nullable = false)
    private String senha;

    @NotNull
    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dt_nascimento;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserRestricao> restricoes; 
}