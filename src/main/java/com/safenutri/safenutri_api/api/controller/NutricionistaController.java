package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.api.service.NutricionistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutricionistas")
public class NutricionistaController {
    @Autowired
    private NutricionistaService service;

    @GetMapping("/{id}")
    public Nutricionista obterPerfil(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping
    public Nutricionista atualizar(@RequestBody Nutricionista nutricionista) {
        return service.atualizarPerfil(nutricionista);
    }
}
