package com.safenutri.safenutri_api.api.service;

import java.time.LocalDateTime;
import java.util.List;

import com.safenutri.safenutri_api.model.output.ContatoOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.output.MensagemOutput;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final JdbcTemplate jdbcTemplate;

    public boolean verificarVinculo(Long usuarioA, Long usuarioB) {
        String sql = "SELECT COUNT(*) FROM paciente " +
                "WHERE (user_id = ? AND nutricionista_id = ?) " +
                "OR (user_id = ? AND nutricionista_id = ?)";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, usuarioA, usuarioB, usuarioB, usuarioA);
        return count != null && count > 0;
    }

    public MensagemOutput salvar(MensagemOutput mensagem) {
        if (!verificarVinculo(mensagem.getRemetenteId(), mensagem.getDestinatarioId())) {
            throw new AccessDeniedException(
                    "Você não tem permissão para enviar mensagens a este usuário pois não há vínculo profissional ativo.");
        }

        String sql = "INSERT INTO mensagem (remetente_id, destinatario_id, conteudo, data_envio) VALUES (?, ?, ?, ?) RETURNING id";
        LocalDateTime agora = LocalDateTime.now();

        Long idSalvo = jdbcTemplate.queryForObject(sql, Long.class,
                mensagem.getRemetenteId(), mensagem.getDestinatarioId(), mensagem.getConteudo(), agora);

        mensagem.setId(idSalvo);
        mensagem.setDataEnvio(agora);
        return mensagem;
    }

    public List<MensagemOutput> buscarHistorico(Long usuarioA, Long usuarioB) {
        String sql = "SELECT id, remetente_id, destinatario_id, conteudo, data_envio " +
                "FROM mensagem " +
                "WHERE (remetente_id = ? AND destinatario_id = ?) " +
                "OR (remetente_id = ? AND destinatario_id = ?) " +
                "ORDER BY data_envio ASC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MensagemOutput mensagem = new MensagemOutput(rs.getLong("id"),
                    rs.getLong("remetente_id"),
                    rs.getLong("destinatario_id"),
                    rs.getString("conteudo"),
                    rs.getTimestamp("data_envio").toLocalDateTime());
            return mensagem;
        }, usuarioA, usuarioB, usuarioB, usuarioA);
    }

    public List<ContatoOutput> buscarContatosComUltimaMensagem(Long meuId) {
        String sql = "SELECT u.id, u.nome, " +
                "(SELECT conteudo FROM mensagem m " +
                " WHERE (m.remetente_id = u.id AND m.destinatario_id = ?) " +
                "    OR (m.remetente_id = ? AND m.destinatario_id = u.id) " +
                " ORDER BY data_envio DESC LIMIT 1) as ultima_mensagem, " +
                "n.imagem_perfil as imagem " +
                "FROM \"user\" u " +
                "JOIN paciente p ON (p.user_id = u.id OR p.nutricionista_id = u.id) " +
                "LEFT JOIN nutricionista n ON n.user_id = u.id " +
                "WHERE (p.user_id = ? OR p.nutricionista_id = ?) AND u.id != ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ContatoOutput contato = new ContatoOutput();
            contato.setId(rs.getLong("id"));
            contato.setNome(rs.getString("nome"));
            contato.setUltimaMensagem(rs.getString("ultima_mensagem"));
            contato.setImagem(rs.getString("imagem"));
            return contato;
        }, meuId, meuId, meuId, meuId, meuId);
    }
}
