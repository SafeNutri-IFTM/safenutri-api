package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.api.service.TipoReceitaService;
import com.safenutri.safenutri_api.model.entity.TipoReceita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-receita")
public class TipoReceitaController {
    @Autowired
    private TipoReceitaService service;

    @GetMapping
    public List<TipoReceita> listar() {
        return service.listar();
    }
}
