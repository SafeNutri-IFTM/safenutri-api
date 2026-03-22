package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Pode adicionar métodos de busca personalizados aqui, como:
    // Optional<Role> findByRole(String role);
}
