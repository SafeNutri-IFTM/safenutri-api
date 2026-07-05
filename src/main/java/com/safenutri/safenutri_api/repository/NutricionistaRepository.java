package com.safenutri.safenutri_api.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.output.NutricionistaOutput;

@Repository
public interface NutricionistaRepository extends JpaRepository<Nutricionista, Long>{

    Optional<Nutricionista> findByUserId(Long userId);

    Optional<Nutricionista> findByTokenConvite(UUID tokenConvite);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.NutricionistaOutput(u.nome, u.email, u.genero.id, " +
            "u.dtNascimento, u.created, " +
            "(SELECT COUNT(rf) FROM ReceitaFavorita rf WHERE rf.user.id = :userId) AS qntdFavoritas, " +
            "(SELECT COUNT(ra) FROM ReceitaAvaliacao ra WHERE ra.user.id = :userId) AS qntdAvaliadas, " +
            "(SELECT COUNT(r) FROM Receita r WHERE r.user.id = :userId) AS qntdMinhasReceitas, " +
            "(SELECT COUNT(p) FROM Paciente p WHERE p.nutricionista.id = :userId) AS paciente," +
            "n.descPerfil, n.CRN, n.imagem) " +
            "FROM User u " +
            "JOIN Nutricionista n ON u.id = n.user.id " +
            "WHERE u.id = :userId")
    NutricionistaOutput getProfileInfos(@Param("userId") Long userId);
}
