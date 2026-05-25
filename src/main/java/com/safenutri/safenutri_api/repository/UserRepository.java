package com.safenutri.safenutri_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.output.UserOutput;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE :email AND u.activated = TRUE")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.UserOutput(u.nome, u.email, u.genero.id, " +
            "u.dtNascimento, u.created, " +
            "(SELECT COUNT(rf) FROM ReceitaFavorita rf WHERE rf.user.id = :userId) AS qntdFavoritas, " + 
            "(SELECT COUNT(ra) FROM ReceitaAvaliacao ra WHERE ra.user.id = :userId) AS qntdAvaliadas, " +
            "(SELECT COUNT(r) FROM Receita r WHERE r.user.id = :userId) AS qntdMinhasReceitas, " +
            "n.CRN, n.descPerfil) " +
            "FROM User u " +
            "LEFT JOIN u.genero g " +
            "LEFT JOIN Nutricionista n ON n.user.id = u.id " +
            "WHERE u.id = :userId")
    UserOutput getProfileInfos(@Param("userId") Long userId);

}
