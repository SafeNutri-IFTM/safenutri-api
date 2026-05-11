package com.safenutri.safenutri_api.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.ReceitaAvaliacao;

@Repository
public interface ReceitaAvaliacaoRepository extends JpaRepository<ReceitaAvaliacao, Long> {
    Optional<ReceitaAvaliacao> findByReceitaIdAndUserId(Long receitaId, Long userId);
    List<ReceitaAvaliacao> findByReceitaIdOrderByIdDesc(Long receitaId);
}