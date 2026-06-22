package com.safenutri.safenutri_api.core.security.jwt;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.safenutri.safenutri_api.model.entity.User;
import com.safenutri.safenutri_api.model.enums.RoleEnum;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final HttpServletRequest request;

    @Value("${security.jwt.expiration}")
    private String expiration;

    @Value("${security.jwt.key}")
    private String key;

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public String recuperarTokenDoHeader() {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public boolean tokenValido(String token) {
        try {
            if (token == null) {
                return false;
            }

            Claims claims = obterClaims(token);
            return !claims.getExpiration().before(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    public String gerarToken(User user, boolean termosPendentes) {
        long exp = Long.parseLong(expiration);

        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
        Date data = Date.from(dataHoraExpiracao
                .atZone(ZoneId.systemDefault())
                .toInstant());

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getNome());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().getRole());
        claims.put("idRole", user.getRole().getId());

        claims.put("termosPendentes", termosPendentes);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(data)
                .addClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String gerarTokenEmail(User user) {
        long exp = Long.parseLong(expiration);

        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
        Date data = Date.from(dataHoraExpiracao
                .atZone(ZoneId.systemDefault())
                .toInstant());

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(data)
                .addClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaims() {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isEmpty()) {
            throw new RuntimeException("Authorization vazio!");
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization sem Bearer!");
        }

        String token = authorization.substring(7);
        return obterClaims(token);
    }

    public Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException {
        return obterClaims(token).getSubject();
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