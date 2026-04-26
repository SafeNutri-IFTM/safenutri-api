package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.api.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    // Rota EXATA que o Angular está chamando: GET /user/me
    // @GetMapping("/me")
    // public ResponseEntity<User> getProfile(Principal principal) {
    //     if (principal == null) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //     }
    //     User user = userService.getProfileByEmail(principal.getName());
    //     return ResponseEntity.ok(user);
    // }

    // Presumindo que o Front-end também use PUT /user/me para salvar
    // @PutMapping("/me")
    // public ResponseEntity<User> atualizarPerfil(@RequestBody @Valid UserInput input) {
    //     User userAtualizado = userService.atualizarPerfil(input);
    //     return ResponseEntity.ok(userAtualizado);
    // }

    
    // @GetMapping("/{id}")
    // public ResponseEntity<User> getById(@PathVariable Long id) {
    //     User user = userService.getReference(id); 
    //     return ResponseEntity.ok(user);
    // }
    
    @PostMapping
    @Operation(summary = "Cria um usuário", description = "Cria um novo usuário com base nas informações fornecidas")
    public ResponseEntity<Void> save(@RequestBody @Valid UserInput input) {
        userService.save(input);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
