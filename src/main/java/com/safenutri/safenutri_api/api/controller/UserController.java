package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import com.safenutri.safenutri_api.api.service.AtividadeRecenteService;
import com.safenutri.safenutri_api.api.service.UserRestricaoService;
import com.safenutri.safenutri_api.api.service.UserService;
import com.safenutri.safenutri_api.core.security.UserPrincipal;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.enums.RoleEnum;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.model.output.AtividadeRecenteOutput;
import com.safenutri.safenutri_api.model.output.FeedbackOutput;
import com.safenutri.safenutri_api.model.output.RestricaoOutput;
import com.safenutri.safenutri_api.model.output.UserListOutput;
import com.safenutri.safenutri_api.model.output.UserOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserRestricaoService userRestricaoService;
    private final AtividadeRecenteService atividadeRecenteService;

    @GetMapping
    @Operation(summary = "Busca os dados do perfil do usuário autenticado")
    public ResponseEntity<UserOutput> getUserPerfil(
            @AuthenticationPrincipal UserPrincipal user) {
        UserOutput userOutput = userService.getProfileInfos(user.getId());
        return ResponseEntity.ok(userOutput);
    }

    @GetMapping("/restricoes")
    @Operation(summary = "Busca as restrições alimentares do usuário autenticado")
    public ResponseEntity<List<RestricaoOutput>> listaUserRestricoes(
            @AuthenticationPrincipal UserPrincipal user) {
        List<RestricaoOutput> restricoes = userRestricaoService.findRestricoesByUserId(user.getId());
        return ResponseEntity.ok(restricoes);
    }

    @GetMapping("/atividades-recentes")
    @Operation(summary = "Busca as atividades recentes do usuário autenticado")
    public ResponseEntity<List<AtividadeRecenteOutput>> listaUserAtividadesRecentes(
            @AuthenticationPrincipal UserPrincipal user) {
        List<AtividadeRecenteOutput> atividades = atividadeRecenteService.buscarRecentes(user.getId());
        return ResponseEntity.ok(atividades);
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário")
    public ResponseEntity<Void> criaUsuario(
            @RequestBody @Valid UserInput input) {
        userService.save(input);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    @Operation(summary = "Atualiza os dados do perfil do usuário autenticado")
    public ResponseEntity<User> atualizarPerfil(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid UserInput input) {
        User userAtualizado = userService.atualizarPerfil(user.getId(), input);
        return ResponseEntity.ok(userAtualizado);
    }

    @PutMapping("/atualizar-restricoes")
    @Operation(summary = "Atualiza as restrições alimentares do usuário autenticado")
    public ResponseEntity<Void> atualizarRestricoes(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody List<Long> restricoesIdList) {
        User userBd = userService.findById(user.getId());
        userService.processarRestricoes(userBd, restricoesIdList);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate")
    @Operation(summary = "Desativa a conta do usuário autenticado (ou de outro se admin)")
    public ResponseEntity<Void> desativarConta(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(required = false) Long userId) {

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + RoleEnum.ADMIN.getRole()));

        Long targetId = (isAdmin && userId != null) ? userId : user.getId();
        userService.deactivateAccount(targetId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate")
    @Operation(summary = "Ativa a conta do usuário autenticado (ou de outro se admin)")
    public ResponseEntity<Void> ativarConta(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(required = false) Long userId) {

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + RoleEnum.ADMIN.getRole()));

        Long targetId = (isAdmin && userId != null) ? userId : user.getId();
        userService.activateAccount(targetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<UserListOutput>> listarTodosUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean ativo) {
        return ResponseEntity.ok(userService.listarTodosUsuariosPaginado(page, size, ativo));
    }

}
