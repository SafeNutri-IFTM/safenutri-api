package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    @Query("SELECT p.nutricionista.id FROM Paciente p WHERE p.user.id = :userId")
    List<Long> findAllNutricionistaIdByUserId(Long userId);
}
