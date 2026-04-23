package com.safenutri.safenutri_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.ReceitaRestricao;

@Repository
public interface ReceitaRestricaoRepository extends JpaRepository<ReceitaRestricao, Long>{

    @Modifying
    @Query("DELETE FROM ReceitaRestricao rr WHERE rr.receita.id = :receitaId")
    void deleteByReceitaId(Long receitaId);
}
