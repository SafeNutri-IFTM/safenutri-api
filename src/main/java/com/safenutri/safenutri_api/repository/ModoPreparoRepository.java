package com.safenutri.safenutri_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.ModoPreparo;
import com.safenutri.safenutri_api.model.output.ModoPreparoOutput;

@Repository
public interface ModoPreparoRepository extends JpaRepository<ModoPreparo, Long>{
    
    @Modifying
    @Query("DELETE FROM ModoPreparo mp WHERE mp.receita.id = :receitaId")
    void deleteByReceitaId(@Param("receitaId") Long receitaId);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ModoPreparoOutput (mp.passo, " + 
        "mp.titulo, mp.descricao) FROM ModoPreparo mp WHERE mp.receita.id = :id")
    List<ModoPreparoOutput>findByReceitaId(@Param("id") Long id);
}
