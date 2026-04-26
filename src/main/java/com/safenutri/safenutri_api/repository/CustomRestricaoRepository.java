package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.CustomRestricao;

@Repository
public interface CustomRestricaoRepository extends JpaRepository<CustomRestricao, Long> {

    @Query("SELECT cr FROM CustomRestricao cr WHERE cr.user.id = :userId AND cr.activated = TRUE")
    List<CustomRestricao> findByActivated(@Param("userId") Long userId);
}
