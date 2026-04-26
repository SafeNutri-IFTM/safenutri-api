package com.safenutri.safenutri_api.api.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.LoginInput;
import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.model.output.EmailOutput;
import com.safenutri.safenutri_api.api.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Cria um usuário", description = "Cria um novo usuário com base nas informações fornecidas")
    public ResponseEntity<Void> save(@Valid @RequestBody UserInput userInput) {
        userService.save(userInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // @PostMapping("/mudarSenha")
    // @Operation(summary = "Muda a senha do usuário logado")
    // public ResponseEntity<?> updateById(@RequestBody @Valid LoginInput loginInput) {

    //     Optional<User> userOptional = userService.findByExistEmail(loginInput.getEmail());

    //     if (userOptional.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
    //     }

    //     User updatedUser = userOptional.get();

    //     if (!updatedUser.getActivated()) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário desativado.");
    //     }

    //     userService.mudarSenha(updatedUser, loginInput.getPassword());

    //     return ResponseEntity.ok().build();
    // }
}
