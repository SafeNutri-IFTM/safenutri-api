package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safenutri.safenutri_api.api.service.RestricaoAlimentarService;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;

@RestController
@RequiredArgsConstructor
@RequestMapping("restricao-alimentar")
public class RestricaoAlimentarController {
    private final RestricaoAlimentarService restricaoAlimentarService;

    @GetMapping
    @Operation(summary = "Busca todas as restrições ativas que foram aprovadas", description = "Busca todas as restrições alimentares que estão ativas e foram aprovadas")
    public ResponseEntity<List<RestricaoAlimentar>> getAllActivatedAndApproved() {
        List<RestricaoAlimentar> restricoes = restricaoAlimentarService.getAllActivatedAndApproved();
        return ResponseEntity.ok(restricoes);
    }
}
