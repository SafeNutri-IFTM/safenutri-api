package com.safenutri.safenutri_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.AceitoTermos;

@Repository
public interface AceitoTermosRepository extends JpaRepository<AceitoTermos, Long> {
    
    boolean existsByUserIdAndDocumentoId(Long userId, Long documentoId);
}