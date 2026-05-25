package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.RestricaoPessoal;

@Repository
public interface RestricaoPessoalRepository extends JpaRepository<RestricaoPessoal, Long> {

    @Query("SELECT rp FROM RestricaoPessoal rp WHERE rp.user.id = :userId")
    List<RestricaoPessoal> findByActivated(@Param("userId") Long userId);
}
