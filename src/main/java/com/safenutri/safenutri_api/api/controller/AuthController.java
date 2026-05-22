package com.safenutri.safenutri_api.api.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

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

    @PostMapping("/recuperarSenha/{email}")
    @Operation(summary = "Envia um email de recuperação de senha")
    public ResponseEntity<?> recuperarSenha(@PathVariable String email) {
        try {
            userService.recuperarSenha(email);
            return ResponseEntity.ok().body("{\"message\": \"Email enviado com sucesso\"}");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/mudar-senha")
    @Operation(summary = "Valida o token enviado por e-mail e troca a senha")
    public ResponseEntity<?> mudarSenha(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            String novaSenha = payload.get("novaSenha");

            if (token == null || !jwtService.tokenValido(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Link de recuperação inválido ou expirado.");
            }

            Claims claims = jwtService.obterClaims(token);
            String email = claims.getSubject();

            User user = userService.findByExistEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado para este token."));

            userService.mudarSenha(user, novaSenha);

            return ResponseEntity.ok().body("{\"message\": \"Senha alterada com sucesso!\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> loginGoogle(Authentication authentication) {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2User userGoogle = oauthToken.getPrincipal();

        String email = userGoogle.getAttribute("email");
        String nome = userGoogle.getAttribute("name");

        User user = userService.loginGoogle(email, nome);

        String token = jwtService.gerarToken(user);

        String url = "http://localhost:4200/login-success?token=" + token;

        return ResponseEntity.status(302)
                .header("Location", url)
                .build();
    }
}