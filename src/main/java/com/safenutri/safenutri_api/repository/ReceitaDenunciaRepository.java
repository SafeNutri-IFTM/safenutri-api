package com.safenutri.safenutri_api.repository;

import com.safenutri.safenutri_api.model.entity.ReceitaDenuncia;
import com.safenutri.safenutri_api.model.output.ReportReceitaOutput;
import com.safenutri.safenutri_api.model.output.ReportReceitaDetalheOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceitaDenunciaRepository extends JpaRepository<ReceitaDenuncia, Long> {

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReportReceitaOutput(" +
           "rd.id, r.titulo, u.nome, rd.created, rd.descricao) " +
           "FROM ReceitaDenuncia rd JOIN rd.receita r JOIN rd.user u " +
           "WHERE r.activated = :ativo " + 
           "ORDER BY rd.id DESC")
    Page<ReportReceitaOutput> findAllReports(Pageable pageable, @Param("ativo") boolean ativo);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReportReceitaDetalheOutput(" +
           "rd.id, rd.created, u.nome, r.titulo, autorRec.nome, rd.descricao, rd.descricao) " +
           "FROM ReceitaDenuncia rd " +
           "JOIN rd.receita r " +
           "JOIN rd.user u " +
           "JOIN r.user autorRec " +
           "WHERE rd.id = :id")
    ReportReceitaDetalheOutput findReportDetalheById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Receita r SET r.activated = false WHERE r.id = (SELECT rd.receita.id FROM ReceitaDenuncia rd WHERE rd.id = :idReport)")
    void inativarReceitaPorReport(@Param("idReport") Long idReport);

    // CORREÇÃO: status_denuncia_id alterado para status_id
    @Modifying
    @Query(value = "UPDATE receita_denuncia SET status_id = 4 WHERE id = :idReport", nativeQuery = true)
    void atualizarStatusReportParaCorrigindo(@Param("idReport") Long idReport);
}