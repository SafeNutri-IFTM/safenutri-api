package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "versao")
    private String versao;

    @Column(name = "texto")
    private String texto;
}
