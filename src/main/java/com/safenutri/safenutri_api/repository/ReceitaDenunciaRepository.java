package com.safenutri.safenutri_api.repository;

import java.util.List;

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
    ReceitaDenuncia findFirstByReceitaIdOrderByIdDesc(Long receitaId);

    List<ReceitaDenuncia> findByReceitaId(Long receitaId);
    
    List<ReceitaDenuncia> findAllByReceitaIdAndStatus_Status(Long receitaId, String status);

    // ======== NOVA QUERY: Busca APENAS os reports que o nutricionista precisa corrigir ========
    @Query("SELECT rd FROM ReceitaDenuncia rd " +
           "JOIN rd.status s " +
           "WHERE rd.receita.id = :receitaId AND UPPER(s.status) = 'SUSPENSA'")
    List<ReceitaDenuncia> findAllSuspendedReportsByReceitaId(@Param("receitaId") Long receitaId);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReportReceitaOutput(" +
           "rd.id, r.id, r.titulo, u.nome, rd.created, rd.descricao, s.status) " + 
           "FROM ReceitaDenuncia rd " +
           "JOIN rd.receita r " +
           "JOIN rd.user u " +
           "LEFT JOIN rd.status s " +
           "WHERE (:ativo = true AND (s IS NULL OR UPPER(s.status) IN ('PENDENTE', 'AGUARDANDO', 'CONCLUÍDO', 'CONCLUIDO'))) " + 
           "   OR (:ativo = false AND UPPER(s.status) IN ('EM ANÁLISE', 'EM ANALISE', 'SUSPENSA')) " +
           "ORDER BY rd.id DESC")
    Page<ReportReceitaOutput> findAllReports(Pageable pageable, @Param("ativo") boolean ativo);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReportReceitaDetalheOutput(" +
           "rd.id, rd.created, u.nome, r.titulo, autorRec.nome, rd.descricao, r.activated, s.status) " + 
           "FROM ReceitaDenuncia rd " +
           "JOIN rd.receita r " +
           "JOIN rd.user u " +
           "JOIN r.user autorRec " +
           "LEFT JOIN rd.status s " +
           "WHERE rd.id = :id")
    ReportReceitaDetalheOutput findReportDetalheById(@Param("id") Long id);

    @Query("SELECT new com.safenutri.safenutri_api.model.output.ReportReceitaOutput(" +
           "rd.id, r.id, r.titulo, u.nome, rd.created, rd.descricao, s.status) " + 
           "FROM ReceitaDenuncia rd " +
           "JOIN rd.receita r " +
           "JOIN rd.user u " +
           "LEFT JOIN rd.status s " +
           "WHERE u.id = :userId " + // <--- Filtra apenas os reports deste utilizador!
           "  AND ((:ativo = true AND (s IS NULL OR UPPER(s.status) IN ('PENDENTE', 'AGUARDANDO', 'CONCLUÍDO', 'CONCLUIDO'))) " + 
           "   OR (:ativo = false AND UPPER(s.status) IN ('EM ANÁLISE', 'EM ANALISE', 'SUSPENSA'))) " +
           "ORDER BY rd.id DESC")
    Page<ReportReceitaOutput> findMeusReports(Pageable pageable, @Param("ativo") boolean ativo, @Param("userId") Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Receita r SET r.activated = false WHERE r.id = (SELECT rd.receita.id FROM ReceitaDenuncia rd WHERE rd.id = :idReport)")
    void inativarReceitaPorReport(@Param("idReport") Long idReport);

    // 1. EM MASSA: Altera todos os reports da mesma receita para Suspensa (id = 4)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE receita_denuncia SET status_id = 4 WHERE receita_id = (SELECT temp.receita_id FROM (SELECT receita_id FROM receita_denuncia WHERE id = :idReport) AS temp)", nativeQuery = true)
    void atualizarTodosStatusReportParaSuspensa(@Param("idReport") Long idReport);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Receita r SET r.activated = true WHERE r.id = (SELECT rd.receita.id FROM ReceitaDenuncia rd WHERE rd.id = :idReport)")
    void ativarReceitaPorReport(@Param("idReport") Long idReport);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE receita_denuncia SET status_id = 2 WHERE id = :idReport", nativeQuery = true)
    void atualizarStatusReportParaConcluido(@Param("idReport") Long idReport);

    // 2. NOVO: INDIVIDUAL (BLINDADO): Altera APENAS o status deste report específico para Suspensa (id = 4)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE receita_denuncia SET status_id = 4 WHERE id = :idReport", nativeQuery = true)
    void atualizarStatusReportParaSuspensaIndividual(@Param("idReport") Long idReport);

    @Query(value = "SELECT COUNT(*) FROM receita_denuncia WHERE receita_id = (SELECT temp.receita_id FROM (SELECT receita_id FROM receita_denuncia WHERE id = :idReport) AS temp) AND status_id != 2", nativeQuery = true)
    long countReportsNaoConcluidosPorReportId(@Param("idReport") Long idReport);
}