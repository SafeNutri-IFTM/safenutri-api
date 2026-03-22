package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.TipoContato;
import com.safenutri.safenutri_api.api.service.TipoContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-contato")
public class TipoContatoController {
    @Autowired
    private TipoContatoService service;

    @GetMapping
    public List<TipoContato> listar() {
        return service.listar();
    }

    @PostMapping
    public TipoContato criar(@RequestBody TipoContato tipo) {
        return service.salvar(tipo);
    }
}
