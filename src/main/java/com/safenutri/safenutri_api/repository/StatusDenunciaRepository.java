package com.safenutri.safenutri_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.safenutri.safenutri_api.model.entity.StatusDenuncia;

@Repository
public interface StatusDenunciaRepository extends JpaRepository<StatusDenuncia, Long> {
}