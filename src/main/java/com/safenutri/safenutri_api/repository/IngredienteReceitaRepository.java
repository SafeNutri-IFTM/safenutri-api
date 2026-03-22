package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.IngredienteReceita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IngredienteReceitaRepository extends JpaRepository<IngredienteReceita, Long> {
    List<IngredienteReceita> findByReceitaId(Long receitaId);
}
