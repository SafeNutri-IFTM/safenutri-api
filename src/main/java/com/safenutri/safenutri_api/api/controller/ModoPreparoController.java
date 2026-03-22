package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.ModoPreparo;
import com.safenutri.safenutri_api.api.service.ModoPreparoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/passos")
public class ModoPreparoController {
    @Autowired
    private ModoPreparoService service;

    @GetMapping("/receita/{id}")
    public List<ModoPreparo> listarPorReceita(@PathVariable Long id) {
        return service.listarPassos(id);
    }
}
