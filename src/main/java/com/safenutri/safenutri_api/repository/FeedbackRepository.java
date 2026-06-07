package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.Feedback;
import com.safenutri.safenutri_api.model.output.FeedbackOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT new com.safenutri.safenutri_api.model.output.FeedbackOutput(f.id, u.nome, f.descricao, f.created) " +
           "FROM Feedback f JOIN f.user u ORDER BY f.id DESC")
    Page<FeedbackOutput> findAllFeedbacksPaginado(Pageable pageable);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.FeedbackOutput(f.id, u.nome, f.descricao, f.created) " +
           "FROM Feedback f JOIN f.user u WHERE f.id = :id")
    FeedbackOutput findDetalheById(@Param("id") Long id);
}