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
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{

    //SQL PARA RECEITAS QUE ATENDEM AS RESTRIÇÕES DO USER
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput(r.id, tr.tipoReceita, "+
        "r.titulo, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0)) " +
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "WHERE rr.restricao.id IN :restricoesIds " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.tempoPreparo, r.calorias " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    Page<ReceitaDashboardOutput> findByRestricoesId(@Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes, Pageable pageable);

    //SQL PARA RECEITAS QUE ATENDEM AS RESTRIÇÕES DO USER LEVANDO EM CONSIDEDRAÇÃO QUE ELE É UM PACIENTE
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput(r.id, tr.tipoReceita, "+
        "r.titulo, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0)) " +
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "WHERE rr.restricao.id IN :restricoesIds " +
        "GROUP BY r.id, r.user.id, tr.tipoReceita, r.titulo, r.tempoPreparo, r.calorias " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " +
        "ORDER BY (CASE WHEN r.user.id IN :nutriIds THEN 0 ELSE 1 END) ASC, " +
        "COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    Page<ReceitaDashboardOutput> findByRestricoesIdAndNutricionistaId(@Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes, @Param("nutriIds") List<Long> nutriIds, Pageable pageable);

    //SQL PARA O TOP 3 RECEITAS MAIS BEM AVALIADAS QUE ATENDEM AS RESTRIÇÕES DO USER
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput(r.id, tr.tipoReceita, "+
        "r.titulo, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0)) " +
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "WHERE rr.restricao.id IN :restricoesIds " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.tempoPreparo, r.calorias " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " + 
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC "
    )
    List<ReceitaDashboardOutput> findTop3ByRestricoesIds(
        @Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes,
        Pageable pageable
    );

    //SQL PARA O TOP 3 GERAL DAS RECEITAS MAIS BEM AVALIADAS
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaDashboardOutput(r.id, tr.tipoReceita, "+
        "r.titulo, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0)) " +
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.tempoPreparo, r.calorias " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    List<ReceitaDashboardOutput> findTop3(Pageable pageable);
    
    @Query("SELECT COUNT(*) as total FROM Receita r WHERE r.user.id = :nutricionistaId")
    Long countByNutricionistaId(@Param("nutricionistaId") Long nutricionistaId);

    //SQL PARA RETORNAR A QNTD DE RECEITAS QUE UM NUTRICIONISTA TEM CADASTRADA, AGRUPADAS POR TIPO DE RESTRICAO
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput(rr.restricao.restricao , COUNT(r.id) AS total) "+ 
        "FROM Receita r " + 
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "WHERE r.user.id = :nutricionistaId " +
        "GROUP BY rr.restricao.id, rr.restricao.restricao " +
        "ORDER BY COUNT(r.id) DESC"
    )
    List<ReceitaPieChartOutput> countByNutricionistaIdAndGroupByRestricoes(@Param("nutricionistaId") Long nutricionistaId);

}
