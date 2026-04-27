package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.ReceitaRestricao;
import com.safenutri.safenutri_api.model.output.ReceitaRestricaoOutput;

@Repository
public interface ReceitaRestricaoRepository extends JpaRepository<ReceitaRestricao, Long>{

    @Modifying
    @Query("DELETE FROM ReceitaRestricao rr WHERE rr.receita.id = :receitaId")
    void deleteByReceitaId(Long receitaId);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaRestricaoOutput(rr.restricao.id, rr.restricao.restricao) " +
    "FROM ReceitaRestricao rr WHERE rr.receita.id = :receitaId")
    List<ReceitaRestricaoOutput> findByReceitaId(@Param("receitaId") Long receitaId);
}
