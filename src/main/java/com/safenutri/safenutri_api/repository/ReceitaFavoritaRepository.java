package com.safenutri.safenutri_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.ReceitaFavorita;
import com.safenutri.safenutri_api.model.output.ReceitaCardOutput;

@Repository
public interface ReceitaFavoritaRepository extends JpaRepository<ReceitaFavorita, Long> {

    boolean existsByUserIdAndReceitaId(Long userId, Long receitaId);

    void deleteByUserIdAndReceitaId(Long userId, Long receitaId);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReceitaCardOutput(" +
            "r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, " +
            "COALESCE(AVG(ra.qntdEstrela), 0.0), true, r.activated) " +
            "FROM ReceitaFavorita rf " +
            "JOIN rf.receita r " +
            "JOIN r.tipoReceita tr " +
            "LEFT JOIN ReceitaAvaliacao ra ON ra.receita.id = r.id " +
            "WHERE rf.user.id = :userId " +
            "AND LOWER(r.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) " + // 👈 Linha adicionada para o filtro
            "GROUP BY r.id, tr.tipoReceita, r.titulo, r.descricao, r.tempoPreparo, r.calorias, r.activated, rf.created "
            +
            "ORDER BY rf.created DESC")
    Page<ReceitaCardOutput> findFavoritasByUserId(@Param("userId") Long userId, @Param("termo") String termo,
            Pageable pageable);
}
