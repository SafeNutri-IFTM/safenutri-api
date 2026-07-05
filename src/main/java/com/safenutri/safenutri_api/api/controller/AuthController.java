package com.safenutri.safenutri_api.api.controller;

import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.safenutri.safenutri_api.core.security.jwt.JwtService;
import com.safenutri.safenutri_api.api.service.UserService;
import com.safenutri.safenutri_api.api.service.NutricionistaService;
import com.safenutri.safenutri_api.exception.SenhaInvalidaException;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.input.LoginInput;
import com.safenutri.safenutri_api.model.output.Token;
import com.safenutri.safenutri_api.repository.AceitoTermosRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final NutricionistaService nutricionistaService;
    private final UserService userService;
    private final AceitoTermosRepository aceitoTermosRepository;

    @Value("${urlFrontend}")
    private String urlFrontend;

    @GetMapping("/claims")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Retornar claims usados para criar o token")
    public ResponseEntity<Claims> obterClaims() {
        try {
            String token = jwtService.recuperarTokenDoHeader();
            return ResponseEntity.ok(jwtService.obterClaims(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/oauth2/success")
    @Operation(summary = "Lida com o redirecionamento de sucesso do Google OAuth2")
    public ResponseEntity<?> loginGoogle(@RequestAttribute("userGoogle") OAuth2User userGoogle) {
        String email = userGoogle.getAttribute("email");
        String nome = userGoogle.getAttribute("name");
        String foto = userGoogle.getAttribute("picture");

        try {
            User user = userService.loginGoogle(email, nome);

            boolean pendente = aceitoTermosRepository.possuiTermosPendentes(user.getId(), user.getRole().getRole());
            String token = jwtService.gerarToken(user, pendente);

            String url = urlFrontend + "/login?token=" + token + "&termosPendentes=" + pendente;
            return ResponseEntity.status(302).header("Location", url).build();

        } catch (RuntimeException e) {
            if ("USUARIO_NAO_ENCONTRADO_GOOGLE".equals(e.getMessage())) {
                String url = urlFrontend + "/login?register_google=true&email=" + email + "&nome=" + nome + "&foto="
                        + foto;
                return ResponseEntity.status(302).header("Location", url).build();
            }

            String errorUrl = urlFrontend + "/login?error=email_exists_local";
            return ResponseEntity.status(302).header("Location", errorUrl).build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Valida as credenciais do usuário para entrar no sistema")
    public Token login(@RequestBody LoginInput loginInput) {
        try {
            User user = userService.autenticarPorEmail(loginInput.getEmail(), loginInput.getPassword());

            boolean pendente = aceitoTermosRepository.possuiTermosPendentes(user.getId(), user.getRole().getRole());
            String token = jwtService.gerarToken(user, pendente);

            return new Token(user.getEmail(), token, pendente);
        } catch (SenhaInvalidaException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/verify-token")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Verifica o token do usuário")
    public boolean verifyToken() {
        String tokenLimpo = jwtService.recuperarTokenDoHeader();
        return jwtService.tokenValido(tokenLimpo);
    }

    @PostMapping("/recuperar-senha/{email}")
    @SecurityRequirement(name = "bearerAuth")
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

    @PostMapping("/google/register-user")
    @Operation(summary = "Finaliza o cadastro Google como Usuário Comum")
    public ResponseEntity<?> registerGoogleUser(@RequestBody Map<String, Object> payload) {
        try {
            String email = (String) payload.get("email");
            String nome = (String) payload.get("nome");

            @SuppressWarnings("unchecked")
            List<Long> restricoesIds = (List<Long>) payload.get("restricoes");

            User user = userService.cadastrarUsuarioComumGoogle(email, nome, restricoesIds);

            String token = jwtService.gerarToken(user, false);

            return ResponseEntity.ok(new Token(user.getEmail(), token, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/google/register-nutri")
    @Operation(summary = "Finaliza o cadastro Google como Nutricionista, validando CRN")
    public ResponseEntity<?> registerGoogleNutri(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String nome = payload.get("nome");
            String foto = payload.get("foto");
            String crnEstado = payload.get("crnEstado");
            String crnNumero = payload.get("crnNumero");

            nutricionistaService.cadastrarNutricionistaGoogle(email, nome, foto, crnEstado, crnNumero);

            User user = userService.findByExistEmail(email)
                    .orElseThrow(() -> new RuntimeException("Erro interno ao buscar usuário após cadastro."));

            String token = jwtService.gerarToken(user, false);

            return ResponseEntity.ok(new Token(user.getEmail(), token, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        }
    }
}