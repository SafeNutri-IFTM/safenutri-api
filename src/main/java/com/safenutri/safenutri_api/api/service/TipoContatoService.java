package com.safenutri.safenutri_api.api.service;

import com.safenutri.safenutri_api.model.entity.TipoContato;
import com.safenutri.safenutri_api.repository.TipoContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoContatoService {
    @Autowired
    private TipoContatoRepository repository;

    public List<TipoContato> listar() {
        return repository.findAll();
    }

    public TipoContato salvar(TipoContato tipo) {
        return repository.save(tipo);
    }
}
