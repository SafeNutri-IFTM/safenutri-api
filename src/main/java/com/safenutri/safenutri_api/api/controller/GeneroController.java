package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safenutri.safenutri_api.api.service.GeneroService;
import com.safenutri.safenutri_api.model.entity.Genero;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genero")
public class GeneroController {
    private final GeneroService generoService;

    @GetMapping
    @Operation(summary = "Busca os generos", description = "Busca todos os generos")
    public ResponseEntity<?> getAll() {
        List<Genero> generos = generoService.getAll();
        return ResponseEntity.ok(generos);
    }
}
