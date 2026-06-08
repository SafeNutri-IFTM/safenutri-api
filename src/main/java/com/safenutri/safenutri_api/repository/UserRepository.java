package com.safenutri.safenutri_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.output.UserOutput;
import com.safenutri.safenutri_api.model.output.UserPieChartOutput;
import com.safenutri.safenutri_api.model.projection.QtdUserByMesProjection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE :email AND u.activated = TRUE")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.UserOutput(u.nome, u.email, u.genero.id, " +
            "u.dtNascimento, u.created, " +
            "(SELECT COUNT(rf) FROM ReceitaFavorita rf WHERE rf.user.id = :userId) AS qntdFavoritas, " + 
            "(SELECT COUNT(ra) FROM ReceitaAvaliacao ra WHERE ra.user.id = :userId) AS qntdAvaliadas, " +
            "(SELECT COUNT(r) FROM Receita r WHERE r.user.id = :userId) AS qntdMinhasReceitas) " +
            "FROM User u " +
            "WHERE u.id = :userId")
    UserOutput getProfileInfos(@Param("userId") Long userId);

    @Query("SELECT COUNT(*) AS total FROM User u")
    Long countAll();

    @Query("SELECT new com.safenutri.safenutri_api.model.output.UserPieChartOutput(ur.restricao.restricao , " +
            "COUNT(u.id) AS total) " +
            "FROM User u " +
            "JOIN UserRestricao ur ON ur.user.id = u.id " +
            "GROUP BY ur.restricao.id, ur.restricao.restricao " +
            "ORDER BY COUNT(u.id) DESC")
    List<UserPieChartOutput> countUserGroupByRestricao();

    @Query(value = """
        SELECT 
            DATE_TRUNC('month', created) AS mes,
            COUNT(*) AS qntd
        FROM public.user
        WHERE activated = true
        AND DATE_PART('year', created) = DATE_PART('year', CURRENT_DATE)
        GROUP BY mes
        ORDER BY mes
        """, nativeQuery = true)
    List<QtdUserByMesProjection> getQuantidadeUsuariosByMes();

}
