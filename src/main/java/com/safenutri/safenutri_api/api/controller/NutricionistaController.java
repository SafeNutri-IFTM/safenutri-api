package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.NutricionistaService;
import com.safenutri.safenutri_api.model.input.NutricionistaInput;
import com.safenutri.safenutri_api.model.output.PerfilNutricionistaPublicoOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nutricionista")
public class NutricionistaController {
    private final NutricionistaService nutricionistaService;

    @PostMapping
    @Operation(summary = "Cria um nutricionista", description = "Cria um novo nutricionista com base nas informações fornecidas")
    public ResponseEntity<Void> save(@Valid @RequestBody NutricionistaInput nutricionistaInput) {
        nutricionistaService.save(nutricionistaInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/publico")
    @Operation(summary = "Busca perfil público", description = "Retorna os dados públicos do perfil do nutricionista")
    public ResponseEntity<PerfilNutricionistaPublicoOutput> getPerfilPublico(@PathVariable Long id) {
        return ResponseEntity.ok(nutricionistaService.getPerfilPublico(id));
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> desativarConta() {
        boolean deactivated = nutricionistaService.deactivateAccount();

        if (deactivated) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
