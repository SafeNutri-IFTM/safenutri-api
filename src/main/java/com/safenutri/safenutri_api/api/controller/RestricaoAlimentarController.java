package com.safenutri.safenutri_api.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; // Import necessário
import org.springframework.web.bind.annotation.*; // Import para PostMapping, RequestBody, etc.
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma nova restrição", description = "Salva uma nova restrição alimentar no banco de dados")
    public RestricaoAlimentar adicionar(@RequestBody RestricaoAlimentar restricaoAlimentar) {
        return restricaoAlimentarService.salvar(restricaoAlimentar);
    }
}