package com.safenutri.safenutri_api.repository;

import java.util.List;

import com.safenutri.safenutri_api.model.output.RestricaoOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;

@Repository
public interface RestricaoAlimentarRepository extends JpaRepository<RestricaoAlimentar, Long>{

    @Query("SELECT new com.safenutri.safenutri_api.model.output.RestricaoOutput(r.id, r.restricao) " +
            "FROM RestricaoAlimentar r WHERE r.activated = TRUE ")
    List<RestricaoOutput> findByActivated();
}
