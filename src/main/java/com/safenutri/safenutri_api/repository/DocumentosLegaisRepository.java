package com.safenutri.safenutri_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.DocumentosLegais;

@Repository
public interface DocumentosLegaisRepository extends JpaRepository<DocumentosLegais, Long> {
    // Busca a versão mais recente ativa de um tipo de documento
    Optional<DocumentosLegais> findFirstByTipoAndActivatedTrueOrderByCreatedDesc(String tipo);
}
