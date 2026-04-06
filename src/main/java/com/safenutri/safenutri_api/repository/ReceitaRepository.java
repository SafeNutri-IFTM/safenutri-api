package com.safenutri.safenutri_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.Receita;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{

}
