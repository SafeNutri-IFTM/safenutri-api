package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.IngredienteReceita;
import com.safenutri.safenutri_api.api.service.IngredienteReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ingredientes-receita")
public class IngredienteReceitaController {
    @Autowired
    private IngredienteReceitaService service;

    @PostMapping
    public IngredienteReceita adicionar(@RequestBody IngredienteReceita item) {
        return service.adicionar(item);
    }

    @GetMapping("/receita/{id}")
    public List<IngredienteReceita> listarPorReceita(@PathVariable Long id) {
        return service.buscarPorReceita(id);
    }
}