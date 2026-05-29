package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.Genero;
import com.safenutri.safenutri_api.model.output.GeneroOutput;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long>{

    @Query("SELECT new com.safenutri.safenutri_api.model.output.GeneroOutput(g.id, g.genero) " +
            "FROM Genero g")
    List<GeneroOutput> findAllGeneros();
}
