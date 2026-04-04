package com.safenutri.safenutri_api.api.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.enums.RoleEnum;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${security.jwt.expiration}")
    private String expiration;
    @Value("${security.jwt.key}")
    private String key;

    private final HttpServletRequest request;

    public String recuperarTokenDoHeader() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public boolean tokenValido(String token) {
        try {
            if (token == null)
                return false;
            Claims claims = obterClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String gerarToken(User user) {
        long expString = Long.valueOf(expiration);

        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Date data = Date.from(dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant());

        HashMap<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("name", user.getNome());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().getRole());
        claims.put("idRole", user.getRole().getId());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(data)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String gerarTokenEmail(User user) {
        long expString = Long.valueOf(expiration);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Date data = Date.from(dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant());
        HashMap<String, Object> claims = new HashMap<>();

        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(data)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    private Claims getClaims() {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isEmpty()) {
            throw new RuntimeException("Authorization vazio!");
        }
        if (authorization.split(" ").length == 0) {
            throw new RuntimeException("Authorization sem Bearer!");
        }
        return obterClaims(authorization.split(" ")[1]);
    }

    public Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException {
        return (String) obterClaims(token).getSubject();
    }

    public Long getIdUsuario() {
        return getClaims().get("id", Long.class);
    }

    private String getRole() {
        return getClaims().get("role", String.class);
    }

    public boolean isAdmin() {
        return RoleEnum.ADMIN.getRole().equals(getRole());
    }

    public boolean isNutricionista() {
        return RoleEnum.NUTRICIONISTA.getRole().equals(getRole());
    }

    public boolean isUser() {
        return RoleEnum.USER.getRole().equals(getRole());
    }
}
