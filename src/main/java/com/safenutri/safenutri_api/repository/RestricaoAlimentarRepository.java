package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;

@Repository
public interface RestricaoAlimentarRepository extends JpaRepository<RestricaoAlimentar, Long>{

    List<RestricaoAlimentar> findByActivatedAndApproved(Boolean activated, Boolean approved);
}
