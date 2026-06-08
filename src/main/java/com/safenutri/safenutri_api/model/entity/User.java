package com.safenutri.safenutri_api.model.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_user")
    @SequenceGenerator(sequenceName = "id_seq_user", allocationSize = 1, name = "id_seq_user")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genero_id")
    private Genero genero;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "dt_nascimento")
    private LocalDate dtNascimento;

    @Column(name = "login_provider", nullable = false)
    private String loginProvider;

    public User(Role role, Genero genero, String nome, String email, String senha, LocalDate dtNascimento) {
        this.role = role;
        this.genero = genero;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dtNascimento = dtNascimento;
        this.loginProvider = "LOCAL";
    }
}