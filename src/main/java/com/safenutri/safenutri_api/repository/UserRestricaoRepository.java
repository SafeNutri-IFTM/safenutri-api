package com.safenutri.safenutri_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.UserRestricao;
import com.safenutri.safenutri_api.model.output.UserRestricaoOutput;

@Repository
public interface UserRestricaoRepository extends JpaRepository<UserRestricao, Long> {

    
    @Modifying
    @Query("DELETE FROM UserRestricao ur WHERE ur.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query("SELECT ur.restricao.id FROM UserRestricao ur WHERE ur.user.id = :userId")
    List<Long> findAllByUserId(Long userId);
}
