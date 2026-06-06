package com.safenutri.safenutri_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.safenutri.safenutri_api.model.entity.AceitoTermos;

@Repository
public interface AceitoTermosRepository extends JpaRepository<AceitoTermos, Long> {

    boolean existsByUserIdAndDocumentoId(Long userId, Long documentoId);

    @Query("SELECT COUNT(dl) > 0 FROM DocumentosLegais dl " +
            "WHERE dl.activated = true " +
            "AND (" +
            "   dl.tipo = 'POLITICA_PRIVACIDADE' " +
            "   OR (:role = 'NUTRICIONISTA' AND dl.tipo = 'TERMO_COMPROMISSO') " +
            "   OR (:role = 'USER' AND dl.tipo = 'TERMO_USO_COMUM')" +
            ") " +
            "AND dl.id NOT IN (" +
            "  SELECT act.documento.id FROM AceitoTermos act WHERE act.user.id = :userId" +
            ")")
    boolean possuiTermosPendentes(@Param("userId") Long userId, @Param("role") String role);
}