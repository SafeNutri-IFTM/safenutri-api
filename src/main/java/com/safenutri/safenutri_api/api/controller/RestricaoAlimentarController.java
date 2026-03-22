package com.safenutri.safenutri_api.api.controller;

import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.api.service.RestricaoAlimentarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restricoes")
public class RestricaoAlimentarController {
    @Autowired
    private RestricaoAlimentarService service;

    @GetMapping
    public List<RestricaoAlimentar> listar() {
        return service.listarTodas();
    }

    @PostMapping
    public RestricaoAlimentar criar(@RequestBody RestricaoAlimentar restricao) {
        return service.salvar(restricao);
    }
}
