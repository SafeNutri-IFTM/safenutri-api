package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.Usuario;
import com.safenutri.safenutri_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario cadastrar(Usuario usuario) {
        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");
        }
        // Aqui você futuramente adicionará a criptografia da senha
        return repository.save(usuario);
    }
}