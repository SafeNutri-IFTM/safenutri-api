package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import com.safenutri.safenutri_api.model.output.RestricaoOutput;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.RestricaoAlimentarService;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.input.RestricaoAlimentarInput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restricao-alimentar")
public class RestricaoAlimentarController {
    private final RestricaoAlimentarService restricaoAlimentarService;

    @GetMapping
    @Operation(summary = "Busca todas as restrições alimentares ativas cadastradas no sistema")
    public ResponseEntity<List<RestricaoOutput>> getAllActivated() {
        List<RestricaoOutput> restricoes = restricaoAlimentarService.getAllActivated();
        return ResponseEntity.ok(restricoes);
    }

    @PostMapping
    @Operation(summary = "Cria uma nova restrição alimentar que será visível para todos os usuários")
    public ResponseEntity<Void> saveRestricaoAlimentar(@RequestBody RestricaoAlimentarInput restricaoAlimentarInput) {
        restricaoAlimentarService.salvar(restricaoAlimentarInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}