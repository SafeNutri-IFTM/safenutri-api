package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.Ingrediente;
import com.safenutri.safenutri_api.api.service.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {
    @Autowired
    private IngredienteService service;

    @PostMapping
    public Ingrediente criar(@RequestBody Ingrediente ingrediente) {
        return service.salvar(ingrediente);
    }

    @GetMapping
    public List<Ingrediente> listar() {
        return service.listar();
    }
}