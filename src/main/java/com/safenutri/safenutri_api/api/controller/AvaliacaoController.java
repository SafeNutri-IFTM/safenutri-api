package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.Avaliacao;
import com.safenutri.safenutri_api.api.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {
    @Autowired
    private AvaliacaoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Avaliacao criar(@RequestBody Avaliacao avaliacao) {
        return service.avaliar(avaliacao);
    }
}
