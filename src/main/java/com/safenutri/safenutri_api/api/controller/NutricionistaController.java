package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.api.service.NutricionistaService;
import com.safenutri.safenutri_api.core.security.UserPrincipal;
import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.input.NutricionistaInput;
import com.safenutri.safenutri_api.model.output.PerfilNutricionistaPublicoOutput;
import com.safenutri.safenutri_api.model.output.NutricionistaOutput;


@RestController
@RequiredArgsConstructor
@RequestMapping("/nutricionista")
public class NutricionistaController {
    private final NutricionistaService nutricionistaService;

    @GetMapping
    @Operation(summary = "Busca os dados do perfil do nutricionista autenticado")
    public ResponseEntity<NutricionistaOutput> getNutricionistaPerfil(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        NutricionistaOutput nutri = nutricionistaService.getProfileInfos(user.getId());
        return ResponseEntity.ok(nutri);
    }

    @GetMapping("/{id}/publico")
    @Operation(summary = "Busca os dados do perfil público do nutricionista autenticado")
    public ResponseEntity<PerfilNutricionistaPublicoOutput> getPerfilPublico(@PathVariable Long id) {
        return ResponseEntity.ok(nutricionistaService.getPerfilPublico(id));
    }

    @GetMapping("/link")
    public ResponseEntity<Map<String, String>> obterLinkConvite(@AuthenticationPrincipal UserPrincipal user) {
        Nutricionista nutri = nutricionistaService.findByUserId(user.getId());
        Map<String, String> response = new HashMap<>();
        response.put("tokenConvite", nutri.getTokenConvite().toString());
        return ResponseEntity.ok(response);
    }
    

    @PostMapping
    @Operation(summary = "Cria um novo nutricionista")
    public ResponseEntity<Void> save(@Valid @RequestBody NutricionistaInput nutricionistaInput) {
        nutricionistaService.save(nutricionistaInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/deactivate")
    @Operation(summary = "Desativa a conta do nutricionista autenticado")
    public ResponseEntity<Void> desativarConta(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        nutricionistaService.deactivateAccount(user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/activate")
    @Operation(summary = "Ativa a conta do nutricionista autenticado")
    public ResponseEntity<Void> ativarConta(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        nutricionistaService.activateAccount(user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
