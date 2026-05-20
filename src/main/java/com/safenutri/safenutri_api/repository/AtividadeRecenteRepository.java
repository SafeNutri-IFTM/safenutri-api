package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.ReceitaAvaliacao;
import com.safenutri.safenutri_api.model.projection.AtividadeRecenteUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AtividadeRecenteRepository extends JpaRepository<ReceitaAvaliacao, Long> {

    @Query(value = """
            SELECT * FROM (
                SELECT 'AVALIACAO' AS tipoAtividade, r.id AS receitaId, r.titulo AS receitaTitulo,
                ra.qntd_estrela AS estrela, ra.updated AS dataAtividade
                FROM receita_avaliacao ra
                INNER JOIN receita r ON r.id = ra.receita_id
                WHERE ra.user_id = :userId
                            
                UNION ALL
                            
                SELECT 'FAVORITO' AS tipoAtividade, r.id AS receitaId, r.titulo AS receitaTitulo,
                NULL AS estela, rf.created AS dataAtividade
                FROM receita_favorita rf
                INNER JOIN receita r ON r.id = rf.receita_id
                WHERE rf.user_id = :userId
            ) atividades
            ORDER BY dataAtividade DESC
            LIMIT 3
            """, nativeQuery = true)
    List<AtividadeRecenteUserProjection> buscarAtividadesRecentes(@Param("userId") Long userId);
}
