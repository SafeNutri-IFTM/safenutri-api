package com.safenutri.safenutri_api.api.controller;

import java.util.List;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.model.output.UserOutput;
import com.safenutri.safenutri_api.model.output.AtividadeRecenteOutput;
import com.safenutri.safenutri_api.api.service.AtividadeRecenteService;
import com.safenutri.safenutri_api.api.service.JwtService;
import com.safenutri.safenutri_api.api.service.UserRestricaoService;
import com.safenutri.safenutri_api.api.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRestricaoService userRestricaoService;
    private final AtividadeRecenteService atividadeRecenteService;

    @GetMapping
    @Operation(summary = "Busca os dados do perfil do usuário autenticado")
    public ResponseEntity<UserOutput> getUserPerfil() {
        UserOutput userOutput = userService.getProfileInfos();
        return ResponseEntity.ok(userOutput);
    }

    @GetMapping("/restricoes")
    @Operation(summary = "Busca as restrições alimentares do usuário autenticado")
    public ResponseEntity<List<RestricaoAlimentar>> listaUserRestricoes() {
        Long userId = jwtService.getIdUsuario();
        List<RestricaoAlimentar> restricoes = userRestricaoService.findRestricoesByUserId(userId);
        return ResponseEntity.ok(restricoes);
    }

    @GetMapping("/atividades-recentes")
    @Operation(summary = "Busca as atividades recentes do usuário autenticado")
    public ResponseEntity<List<AtividadeRecenteOutput>> listaUserAtividadesRecentes() {
        List<AtividadeRecenteOutput> atividades =  atividadeRecenteService.buscarRecentes();
        return ResponseEntity.ok(atividades);
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário")
    public ResponseEntity<Void> save(@RequestBody @Valid UserInput input) {
        userService.save(input);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    @Operation(summary = "Atualiza os dados do perfil do usuário autenticado")
    public ResponseEntity<User> atualizarPerfil(@RequestBody @Valid UserInput input) {
        User userAtualizado = userService.atualizarPerfil(input);
        return ResponseEntity.ok(userAtualizado);
    }

    @PutMapping("/atualizar-restricoes")
    @Operation(summary = "Atualiza as restrições alimentares do usuário autenticado")
    public ResponseEntity<Void> atualizarRestricoes(@RequestBody List<Long> restricoesIdList) {
        Long userId = jwtService.getIdUsuario();
        User user = userService.findById(userId);
        userService.processarRestricoes(user, restricoesIdList);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate")
    @Operation(summary = "Desativa a conta do usuário autenticado")
    public ResponseEntity<Void> desativarConta() {
        boolean deactivated = userService.deactivateAccount();

        if (deactivated) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @PatchMapping("/activate")
//    @Operation(summary = "Ativa a conta do usuário autenticado")
//    public ResponseEntity<Void> ativarConta() {
//    }

}
