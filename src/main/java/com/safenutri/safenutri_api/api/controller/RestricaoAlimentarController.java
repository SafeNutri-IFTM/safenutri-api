package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.RestricaoAlimentarService;
import com.safenutri.safenutri_api.model.input.RestricaoAlimentarInput;
import com.safenutri.safenutri_api.model.output.RestricaoListOutput;
import com.safenutri.safenutri_api.model.output.RestricaoOutput;

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

    @GetMapping("/todas")
    @Operation(summary = "Busca todas as restrições alimentares ativas cadastradas no sistema com descrição")
    public ResponseEntity<Page<RestricaoListOutput>> getAllActivatedComDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean ativo) {
        Page<RestricaoListOutput> pageResult = restricaoAlimentarService.getAllActivatedComDesc(page, size, ativo);
        return ResponseEntity.ok(pageResult);
    }

    @PostMapping
    @Operation(summary = "Cria uma nova restrição alimentar que será visível para todos os usuários")
    public ResponseEntity<Void> saveRestricaoAlimentar(
            @RequestBody RestricaoAlimentarInput restricaoAlimentarInput) {
        restricaoAlimentarService.salvar(restricaoAlimentarInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}