package com.safenutri.safenutri_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.ModoPreparo;

@Repository
public interface ModoPreparoRepository extends JpaRepository<ModoPreparo, Long>{
    
    @Modifying
    @Query("DELETE FROM ModoPreparo mp WHERE mp.receita.id = :receitaId")
    void deleteByReceitaId(Long receitaId);
}
