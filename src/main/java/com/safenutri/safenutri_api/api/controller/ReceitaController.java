package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.Receita;
import com.safenutri.safenutri_api.api.service.ReceitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService service;

    @PostMapping
    public ResponseEntity<Receita> criar(@Valid @RequestBody Receita receita) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(receita));
    }

    @GetMapping
    public List<Receita> listar() {
        return service.listarTodas();
    }
}