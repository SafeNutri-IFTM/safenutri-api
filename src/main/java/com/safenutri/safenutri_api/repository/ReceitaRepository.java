package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    // Aqui você pode adicionar buscas personalizadas, como findByTitulo
}