package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;
import com.safenutri.safenutri_api.model.output.ReceitaPieChartOutput;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{

    //SQL PARA RECEITAS QUANDO O USUÁRIO NÃO TEM RESTRIÇÕES
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(" +
        "r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, " +
        "COALESCE(AVG(ra.qntdEstrela), 0.0), " +
        "(CASE WHEN f.id IS NOT NULL THEN true ELSE false END), r.activated) " +
        "FROM Receita r " +
        "JOIN Nutricionista n ON n.user.id = r.user.id " +
        "JOIN r.tipoReceita tr " +
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "LEFT JOIN ReceitaFavorita f ON f.receita.id = r.id AND f.user.id = :userId " +
        "WHERE r.publicBoolean = TRUE AND r.activated = TRUE " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, f.id " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    Page<ReceitaCardOutput> findReceitasForUserWithNoRestrictions(@Param("userId") Long userId, Pageable pageable);

    //SQL PARA RECEITAS QUE ATENDEM AS RESTRIÇÕES DO USER
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(" +
        "r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, " +
        "COALESCE(AVG(ra.qntdEstrela), 0.0), " +
        "(CASE WHEN f.id IS NOT NULL THEN true ELSE false END), r.activated) " +
        "FROM Receita r " +
        "JOIN Nutricionista n ON n.user.id = r.user.id " +
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "LEFT JOIN ReceitaFavorita f ON f.receita.id = r.id AND f.user.id = :userId " + 
        "WHERE rr.restricao.id IN :restricoesIds AND r.publicBoolean = TRUE AND r.activated = TRUE " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, f.id " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    Page<ReceitaCardOutput> findByRestricoesId(@Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes, @Param("userId") Long userId, Pageable pageable);

    //SQL PARA RECEITAS QUE ATENDEM AS RESTRIÇÕES DO USER LEVANDO EM CONSIDEDRAÇÃO QUE ELE É UM PACIENTE
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(r.id, tr.tipoReceita, "+
        "r.titulo, r.descricao, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0), " +
        "(CASE WHEN f.id IS NOT NULL THEN true ELSE false END), r.activated) " +
        "FROM Receita r " +
        "JOIN Nutricionista n ON n.user.id = r.user.id " +
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "LEFT JOIN ReceitaFavorita f ON f.receita.id = r.id AND f.user.id = :userId " +
        "WHERE rr.restricao.id IN :restricoesIds AND r.publicBoolean = TRUE AND r.activated = TRUE " +
        "GROUP BY r.id, r.user.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, f.id " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " +
        "ORDER BY (CASE WHEN r.user.id IN :nutriIds THEN 0 ELSE 1 END) ASC, " +
        "COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    Page<ReceitaCardOutput> findByRestricoesIdAndNutricionistaId(
        @Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes, 
        @Param("nutriIds") List<Long> nutriIds, 
        @Param("userId") Long userId, 
        Pageable pageable);

    //SQL PARA O TOP 3 RECEITAS MAIS BEM AVALIADAS PARA USUÁRIOS SEM RESTRIÇÕES
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(r.id, tr.tipoReceita, "+
        "r.titulo, r.descricao, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0), " +
        "(CASE WHEN f.id IS NOT NULL THEN true ELSE false END), r.activated) " +
        "FROM Receita r " +
        "JOIN Nutricionista n ON n.user.id = r.user.id " +
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "LEFT JOIN ReceitaFavorita f ON f.receita.id = r.id AND f.user.id = :userId " +
        "WHERE r.publicBoolean = TRUE AND r.activated = TRUE " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, f.id " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC "
    )
    List<ReceitaCardOutput> findTop3ReceitasForUserWithNoRestrictions(@Param("userId") Long userId, Pageable pageable);

    //SQL PARA O TOP 3 RECEITAS MAIS BEM AVALIADAS QUE ATENDEM AS RESTRIÇÕES DO USER
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(r.id, tr.tipoReceita, "+
        "r.titulo, r.descricao, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0), " +
        "(CASE WHEN f.id IS NOT NULL THEN true ELSE false END), r.activated) " +
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " +
        "JOIN Nutricionista n ON n.user.id = r.user.id " +
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "LEFT JOIN ReceitaFavorita f ON f.receita.id = r.id AND f.user.id = :userId " +
        "WHERE rr.restricao.id IN :restricoesIds AND r.publicBoolean = TRUE AND r.activated = TRUE " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, f.id " +
        "HAVING COUNT(DISTINCT rr.restricao.id) = :qntdRestricoes " + 
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC "
    )
    List<ReceitaCardOutput> findTop3ByRestricoesIds(
        @Param("restricoesIds") List<Long> restricoesIds, 
        @Param("qntdRestricoes") Long qntdRestricoes,
        @Param("userId") Long userId,
        Pageable pageable
    );

    //SQL PARA O TOP 3 GERAL DAS RECEITAS MAIS BEM AVALIADAS
    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(r.id, tr.tipoReceita, "+
        "r.titulo, r.descricao, r.tempoPreparo, r.calorias, COALESCE(AVG(ra.qntdEstrela), 0.0), " +
        "(CASE WHEN f.id IS NOT NULL THEN true ELSE false END), r.activated) " + 
        "FROM Receita r " + 
        "JOIN r.tipoReceita tr " +
        "JOIN Nutricionista n ON n.user.id = r.user.id " +
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "JOIN ReceitaRestricao rr ON rr.receita.id = r.id " +
        "LEFT JOIN ReceitaFavorita f ON f.receita.id = r.id AND f.user.id = :userId " +
        "WHERE r.publicBoolean = TRUE AND r.activated = TRUE " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, f.id " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC"
    )
    List<ReceitaCardOutput> findTop3(@Param("userId") Long userId, Pageable pageable);

    //SQL PARA RETORNAR A QNTD DE RECEITAS QUE UM NUTRICIONISTA TEM
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

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(" +
        "r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, " +
        "COALESCE(AVG(ra.qntdEstrela), 0.0), " +
        "(CASE WHEN f.id IS NOT NULL THEN true ELSE false END), r.activated) " +
        "FROM Receita r " +
        "JOIN Nutricionista n ON n.user.id = r.user.id " +
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
        "LEFT JOIN ReceitaFavorita f ON f.receita.id = r.id AND f.user.id = :userId " +
        "WHERE LOWER(r.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, f.id " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC, r.id ASC"
    )
    Page<ReceitaCardOutput> findByTituloPaginado(@Param("termo") String termo, @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(" +
        "r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, " +
        "COALESCE(AVG(ra.qntdEstrela), 0.0), " +
        "(CASE WHEN f.id IS NOT NULL THEN true ELSE false END), r.activated) " +
        "FROM Receita r " +
        "JOIN r.tipoReceita tr " + 
        "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " + 
        "LEFT JOIN ReceitaFavorita f ON f.receita.id = r.id AND f.user.id = :userId " +
        "WHERE r.user.id = :userId " +
        "GROUP BY r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, f.id " +
        "ORDER BY COALESCE(AVG(ra.qntdEstrela), 0.0) DESC, r.id ASC"
    )
    Page<ReceitaCardOutput> getMinhasReceitas(@Param("userId") Long userId, Pageable pageable);

}