package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.Paciente;
import com.safenutri.safenutri_api.api.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService service;

    @PostMapping
    public Paciente vincular(@RequestBody Paciente paciente) {
        return service.vincularNutricionista(paciente);
    }
}
