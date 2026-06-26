package com.safenutri.safenutri_api.api.controller;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safenutri.safenutri_api.api.service.PacienteService;
import com.safenutri.safenutri_api.core.security.jwt.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paciente")
public class PacienteController {
    private final PacienteService pacienteService;

    @PostMapping("/vincular/{tokenConvite}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Víncula o usuário autenticado a um nutricionista")
    public ResponseEntity<Void> vincularPaciente(
        @PathVariable UUID tokenConvite, 
        @AuthenticationPrincipal UserPrincipal user
    ) {
        pacienteService.vincularPorToken(tokenConvite, user.getId());
        return ResponseEntity.ok().build();
    }
    
}
