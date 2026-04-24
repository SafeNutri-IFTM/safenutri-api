package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput(r.caminhoFoto, tr.tipoReceita, "+
        "r.titulo, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0)) " +
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "WHERE rr.restricao.id IN :restricoesIds " +
        "GROUP BY r.id, r.caminhoFoto, tr.tipoReceita, r.titulo, r.tempoPreparo, r.calorias " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    Page<ReceitaDashboardOutput> findByRestricoesId(@Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes, Pageable pageable);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput(r.caminhoFoto, tr.tipoReceita, "+
        "r.titulo, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0)) " +
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "WHERE rr.restricao.id IN :restricoesIds " +
        "GROUP BY r.id, r.user.id, r.caminhoFoto, tr.tipoReceita, r.titulo, r.tempoPreparo, r.calorias " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " +
        "ORDER BY (CASE WHEN r.user.id IN :nutriIds THEN 0 ELSE 1 END) ASC, " +
        "COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    Page<ReceitaDashboardOutput> findByRestricoesIdAndNutricionistaId(@Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes, @Param("nutriIds") List<Long> nutriIds, Pageable pageable);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput(r.caminhoFoto, tr.tipoReceita, "+
        "r.titulo, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0)) " +
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "WHERE rr.restricao.id IN :restricoesIds " +
        "GROUP BY r.id, r.caminhoFoto, tr.tipoReceita, r.titulo, r.tempoPreparo, r.calorias " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " + 
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    List<ReceitaDashboardOutput> findTop3ByRestricoesIds(
        @Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes
    );
}
