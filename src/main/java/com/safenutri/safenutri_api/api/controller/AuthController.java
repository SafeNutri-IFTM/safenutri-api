package com.safenutri.safenutri_api.api.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.safenutri.safenutri_api.api.service.JwtService;
import com.safenutri.safenutri_api.api.service.UserService;
import com.safenutri.safenutri_api.exception.SenhaInvalidaException;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.LoginInput;
import com.safenutri.safenutri_api.model.output.Token;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Valida as credenciais do usuário para entrar no sistema")
    public Token login(@RequestBody LoginInput loginInput) {
        try {
            User user = userService.autenticarPorEmail(loginInput.getEmail(), loginInput.getPassword());

            String token = jwtService.gerarToken(user);

            return new Token(user.getEmail(), token);

        } catch (SenhaInvalidaException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/verify-token")
    @Operation(summary = "Verifica o token do usuário")
    public boolean verifyToken() {
        String tokenLimpo = jwtService.recuperarTokenDoHeader();
        return jwtService.tokenValido(tokenLimpo);
    }

    @GetMapping("/claims")
    @Operation(summary = "Retornar claims usados para criar o token")
    public ResponseEntity<Claims> obterClaims() {
        try {
            String token = jwtService.recuperarTokenDoHeader();
            return ResponseEntity.ok(jwtService.obterClaims(token));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou ausente");
        }
    }
}