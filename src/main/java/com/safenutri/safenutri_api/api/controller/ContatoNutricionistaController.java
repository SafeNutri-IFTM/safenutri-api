package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.ContatoNutricionista;
import com.safenutri.safenutri_api.api.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contatos-nutricionista")
public class ContatoNutricionistaController {
    @Autowired
    private ContatoService service;

    @PostMapping
    public ContatoNutricionista salvar(@RequestBody ContatoNutricionista contato) {
        return service.salvar(contato);
    }
}
