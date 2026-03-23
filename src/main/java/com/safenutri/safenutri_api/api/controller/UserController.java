package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.model.input.UserInput;
import com.safenutri.safenutri_api.api.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Cria um usuário", description = "Cria um novo usuário com base nas informações fornecidas")
    public ResponseEntity<?> save(@Valid @RequestBody UserInput userInput) {
        userService.save(userInput);
        return ResponseEntity.ok().build();
    }
}
