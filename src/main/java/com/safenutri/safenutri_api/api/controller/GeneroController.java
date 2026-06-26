package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.GeneroService;
import com.safenutri.safenutri_api.model.output.GeneroOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genero")
public class GeneroController {
    private final GeneroService generoService;

    @GetMapping
    @Operation(summary = "Busca todos os generos cadastrados no sistema")
    public ResponseEntity<List<GeneroOutput>> getAll() {
        List<GeneroOutput> generos = generoService.getAll();
        return ResponseEntity.ok(generos);
    }
}
