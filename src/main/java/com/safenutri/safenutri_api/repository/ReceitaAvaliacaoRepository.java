package com.safenutri.safenutri_api.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.ReceitaAvaliacao;

@Repository
public interface ReceitaAvaliacaoRepository extends JpaRepository<ReceitaAvaliacao, Long> {
    Optional<ReceitaAvaliacao> findByReceitaIdAndUserId(Long receitaId, Long userId);
    List<ReceitaAvaliacao> findByReceitaIdOrderByIdDesc(Long receitaId);

    @Query("SELECT COUNT(ra) FROM ReceitaAvaliacao ra WHERE ra.receita.user.id = :userId")
    Long countByReceitaUserId(@Param("userId") Long userId);
}