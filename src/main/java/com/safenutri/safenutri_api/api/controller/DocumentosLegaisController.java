package com.safenutri.safenutri_api.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.safenutri.safenutri_api.core.security.JwtService;
import com.safenutri.safenutri_api.core.security.UserPrincipal;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.output.Token;
import com.safenutri.safenutri_api.api.service.DocumentosLegaisService;
import com.safenutri.safenutri_api.model.input.DocumentosLegaisInput;
import com.safenutri.safenutri_api.model.output.DocumentosLegaisOutput;
import com.safenutri.safenutri_api.repository.AceitoTermosRepository;
import com.safenutri.safenutri_api.repository.UserRepository;

@RestController
@RequestMapping("/documentos-legais")
@RequiredArgsConstructor
public class DocumentosLegaisController {

    private final DocumentosLegaisService documentosLegaisService;
    private final UserRepository userRepository;
    private final AceitoTermosRepository aceitoTermosRepository;
    private final JwtService jwtService;

    @GetMapping("/atual")
    @Operation(summary = "Busca o documento ativo por tipo")
    public ResponseEntity<DocumentosLegaisOutput> buscarAtual(@RequestParam String tipo) {
        return ResponseEntity.ok(documentosLegaisService.buscarAtualPorTipo(tipo));
    }

    @PostMapping
    @Operation(summary = "Cria uma nova versão de um documento legal")
    public ResponseEntity<DocumentosLegaisOutput> criarNovaVersao(@Valid @RequestBody DocumentosLegaisInput dto) {
        DocumentosLegaisOutput novoDocumento = documentosLegaisService.criarNovaVersao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDocumento);
    }

    @PostMapping("/aceitar")
    @Operation(summary = "Registra que o usuário autenticado aceitou a versão atual de um documento")
    public ResponseEntity<Token> aceitarDocumento(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam String tipo) {

        documentosLegaisService.registrarAceiteUsuario(userPrincipal.getId(), tipo);

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean aindaTemPendente = aceitoTermosRepository.possuiTermosPendentes(user.getId(), user.getRole().getRole());

        String novoToken = jwtService.gerarToken(user, aindaTemPendente);

        return ResponseEntity.ok(new Token(user.getEmail(), novoToken, aindaTemPendente));
    }
}