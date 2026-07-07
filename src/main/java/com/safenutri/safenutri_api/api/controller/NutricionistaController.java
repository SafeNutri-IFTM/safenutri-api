package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.safenutri.safenutri_api.api.service.NutricionistaService;
import com.safenutri.safenutri_api.api.service.UploadService;
import com.safenutri.safenutri_api.core.security.jwt.UserPrincipal;
import com.safenutri.safenutri_api.model.entity.Nutricionista;
import com.safenutri.safenutri_api.model.input.NutricionistaInput;
import com.safenutri.safenutri_api.model.output.PerfilNutricionistaPublicoOutput;
import com.safenutri.safenutri_api.model.output.NutricionistaOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nutricionista")
public class NutricionistaController {
    private final NutricionistaService nutricionistaService;
    private final UploadService uploadService;

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Busca os dados do perfil do nutricionista autenticado")
    public ResponseEntity<NutricionistaOutput> getNutricionistaPerfil(
            @AuthenticationPrincipal UserPrincipal user) {
        NutricionistaOutput nutri = nutricionistaService.getProfileInfos(user.getId());
        return ResponseEntity.ok(nutri);
    }

    @GetMapping("/{id}/publico")
    @Operation(summary = "Busca os dados do perfil público do nutricionista autenticado")
    public ResponseEntity<PerfilNutricionistaPublicoOutput> getPerfilPublico(@PathVariable Long id) {
        return ResponseEntity.ok(nutricionistaService.getPerfilPublico(id));
    }

    @GetMapping("/link")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Retorna link de vínculo para pacientes")
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
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Desativa a conta do nutricionista autenticado")
    public ResponseEntity<Void> desativarConta(
            @AuthenticationPrincipal UserPrincipal user) {
        nutricionistaService.deactivateAccount(user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/activate")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Ativa a conta do nutricionista autenticado")
    public ResponseEntity<Void> ativarConta(
            @AuthenticationPrincipal UserPrincipal user) {
        nutricionistaService.activateAccount(user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/foto/{nomeArquivo}")
    @Operation(summary = "Mostra a foto de perfil do nutricionista")
    public ResponseEntity<Resource> getFoto(@PathVariable String nomeArquivo) {
        try {
            Resource resource = uploadService.carregarImagemPerfil(nomeArquivo);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(value = "/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarFoto(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam("foto") MultipartFile foto) {
        try {
            String nomeArquivo = nutricionistaService.atualizarFoto(user.getId(), foto);
            return ResponseEntity.ok(Map.of("nomeArquivo", nomeArquivo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao atualizar foto: " + e.getMessage()));
        }
    }
}
