package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import com.safenutri.safenutri_api.model.defaults.DefaultEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "documentos_legais")
public class DocumentosLegais extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_documentos_legais")
    @SequenceGenerator(name = "id_seq_documentos_legais", sequenceName = "id_seq_documentos_legais", allocationSize = 1)
    private Long id;

    @NotNull
    private String tipo; // "TERMO_USO", "POLITICA_PRIVACIDADE", "TERMO_NUTRICIONISTA"

    @NotNull
    private String versao;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String texto;
}
