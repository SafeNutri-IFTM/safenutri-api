package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import com.safenutri.safenutri_api.model.entity.RestricaoPessoal;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.RestricaoPessoalService;
import com.safenutri.safenutri_api.model.input.RestricaoAlimentarInput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/custom-restricao")
public class RestricaoPessoalController {
    private final RestricaoPessoalService restricaoPessoalService;

    @GetMapping
    @Operation(summary = "Busca todas as restrições ativas que foram aprovadas", description = "Busca todas as restrições alimentares que estão ativas e foram aprovadas")
    public ResponseEntity<List<RestricaoPessoal>> getAllActivated() {
        List<RestricaoPessoal> restricoes = restricaoPessoalService.getAllActivated();
        return ResponseEntity.ok(restricoes);
    }

    @PostMapping
    @Operation(summary = "Cria uma nova restrição", description = "Cria uma nova restrição alimentar no banco de dados")
    public ResponseEntity<Void> saveRestricaoAlimentar(@RequestBody RestricaoAlimentarInput restricaoAlimentarInput) {
        restricaoPessoalService.salvar(restricaoAlimentarInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
