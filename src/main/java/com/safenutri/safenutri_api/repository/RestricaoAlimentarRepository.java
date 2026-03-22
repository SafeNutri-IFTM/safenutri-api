package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestricaoAlimentarRepository extends JpaRepository<RestricaoAlimentar, Long> {
}
