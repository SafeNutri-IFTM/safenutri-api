package com.safenutri.safenutri_api.repository;

import java.util.List;

import com.safenutri.safenutri_api.model.output.RestricaoListOutput;
import com.safenutri.safenutri_api.model.output.RestricaoOutput;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;

@Repository
public interface RestricaoAlimentarRepository extends JpaRepository<RestricaoAlimentar, Long> {

        @Query("SELECT new com.safenutri.safenutri_api.model.output.RestricaoOutput(r.id, r.restricao) " +
                        "FROM RestricaoAlimentar r WHERE r.activated = TRUE ")
        List<RestricaoOutput> findByActivated();

        @Query("SELECT new com.safenutri.safenutri_api.model.output.RestricaoListOutput(r.id, r.restricao, r.descricao) "
                        +
                        "FROM RestricaoAlimentar r WHERE r.activated = :ativo " +
                        "ORDER BY r.id ASC")
        Page<RestricaoListOutput> findByActivatedComDesc(Pageable pageable, @Param("ativo") boolean ativo);

        @Query("SELECT COUNT(*) AS total FROM RestricaoAlimentar r")
        Long countAll();
}
