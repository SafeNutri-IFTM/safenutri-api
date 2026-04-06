package com.safenutri.safenutri_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "aceito_termos")
public class AceitoTermos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq_aceito_termos")
    @SequenceGenerator(name = "id_seq_aceito_termos", sequenceName = "id_seq_aceito_termos", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    private DocumentosLegais documento;

    @Column(name = "data_aceito_termos")
    private LocalDateTime dataAceitoTermos = LocalDateTime.now();

    public AceitoTermos(User user, DocumentosLegais documento) {
        this.user = user;
        this.documento = documento;
    }
}
