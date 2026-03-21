package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.ModoPreparo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ModoPreparoRepository extends JpaRepository<ModoPreparo, Long> {
    List<ModoPreparo> findByReceitaIdOrderByPassoAsc(Long receitaId);
}
