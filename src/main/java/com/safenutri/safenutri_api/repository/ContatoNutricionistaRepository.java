package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.ContatoNutricionista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContatoNutricionistaRepository extends JpaRepository<ContatoNutricionista, Long> {
    List<ContatoNutricionista> findByNutricionistaId(Long nutriId);
}
