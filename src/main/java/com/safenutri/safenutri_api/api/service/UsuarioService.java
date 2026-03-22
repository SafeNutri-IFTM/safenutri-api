package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Usuario;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.repository.UsuarioRepository;
import com.safenutri.safenutri_api.repository.RestricaoAlimentarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private RestricaoAlimentarRepository restricaoRepository;

    /**
     * Regista um novo utilizador no sistema.
     * Verifica se o e-mail já existe para evitar duplicados.
     */
    public Usuario cadastrar(Usuario usuario) {
        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");
        }
        // Nota: A implementação de criptografia de senha (BCrypt) deve ser feita aqui
        // futuramente.
        return repository.save(usuario);
    }

    /**
     * Vincula uma restrição alimentar a um utilizador específico.
     * Alimenta a tabela de junção 'user_restricao' definida no diagrama.
     */
    public Usuario adicionarRestricao(Long usuarioId, Long restricaoId) {
        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        RestricaoAlimentar restricao = restricaoRepository.findById(restricaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restrição não encontrada."));

        // Adiciona a restrição à lista carregada da entidade Usuario
        usuario.getRestricoesRelacionadas().add(restricao);

        return repository.save(usuario);
    }
}