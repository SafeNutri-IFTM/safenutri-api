package com.safenutri.safenutri_api.api.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.entity.RestricaoAlimentar;
import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.entity.UserRestricao;
import com.safenutri.safenutri_api.model.input.UserInput;
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

    // Rota EXATA que o Angular está chamando: GET /user/me
    @GetMapping("/me")
    public ResponseEntity<User> getProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getProfileByEmail(principal.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me/restricoes")
    public ResponseEntity<List<RestricaoAlimentar>> getRestricoes() {

        Long userId = jwtService.getIdUsuario();

        List<RestricaoAlimentar> restricoes = userRestricaoService.findRestricoesByUserId(userId);

        return ResponseEntity.ok(restricoes);
    }

    @PutMapping("/me/remover-restricoes")
    public ResponseEntity<Void> atualizarRestricoes(@RequestBody List<Long> restricoesIdList) {
        Long userId = jwtService.getIdUsuario();

        User user = userService.findById(userId);

        userService.processarRestricoes(user, restricoesIdList);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getReference(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Cria um usuário", description = "Cria um novo usuário com base nas informações fornecidas")
    public ResponseEntity<Void> save(@RequestBody @Valid UserInput input) {
        userService.save(input);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
