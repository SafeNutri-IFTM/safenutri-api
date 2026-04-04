package com.safenutri.safenutri_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE :email AND u.activated = TRUE")
    Optional<User> findByEmail(String email);
}
