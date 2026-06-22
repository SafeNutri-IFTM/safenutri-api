package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    @Query("SELECT p.nutricionista.id FROM Paciente p WHERE p.user.id = :userId")
    List<Long> findAllNutricionistaIdByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Paciente p " +
        "WHERE p.user.id = :userId AND p.nutricionista.id = :nutriId"
    )
    boolean existsByUserIdAndNutricionistaId(@Param("userId") Long userId, @Param("nutriId") Long nutriId);

    @Query("SELECT COUNT(*) as total FROM Paciente p WHERE p.nutricionista.id = :nutricionistaId")
    Long countByNutricionistaId(@Param("nutricionistaId") Long nutricionistaId);
}
