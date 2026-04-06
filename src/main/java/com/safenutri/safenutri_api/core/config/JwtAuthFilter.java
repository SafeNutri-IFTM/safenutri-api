package com.safenutri.safenutri_api.core.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.safenutri.safenutri_api.api.service.JwtService;
import com.safenutri.safenutri_api.api.service.UserService;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        // 1. Recupera o token do header (você já tem esse método no JwtService!)
        String token = jwtService.recuperarTokenDoHeader();

        // 2. Valida se o token existe e é válido
        if (token != null && jwtService.tokenValido(token)) {
            String loginUsuario = jwtService.obterLoginUsuario(token);

            // 3. Carrega o usuário do banco
            // Certifique-se que o seu UserService implementa UserDetailsService
            UserDetails userDetails = userService.loadUserByUsername(loginUsuario);

            // 4. Cria a autenticação do Spring Security
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 5. Coloca o usuário no "contexto" (agora o Spring sabe que ele está logado)
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // Segue o fluxo normal da requisição
        filterChain.doFilter(request, response);
    }
}