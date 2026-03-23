package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.NutricionistaService;
import com.safenutri.safenutri_api.model.input.NutricionistaInput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nutricao")
public class NutricionistaController {
    private final NutricionistaService nutricionistaService;

    @PostMapping
    @Operation(summary = "Cria um nutricionista", description = "Cria um novo nutricionista com base nas informações fornecidas")
    public ResponseEntity<?> save(@Valid @RequestBody NutricionistaInput nutricionistaInput) {
        nutricionistaService.save(nutricionistaInput);
        return ResponseEntity.ok().build();
    }
}
