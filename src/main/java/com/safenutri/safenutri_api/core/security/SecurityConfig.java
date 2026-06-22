package com.safenutri.safenutri_api.core.security;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.safenutri.safenutri_api.core.security.jwt.JwtAuthFilter;
import com.safenutri.safenutri_api.core.security.rateLimit.RateLimitingFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RateLimitingFilter rateLimitingFilter;
    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${urlFrontend}")
    private String urlFrontend;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth2/**", "/login/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers(HttpMethod.POST, "/nutricionista").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/genero").permitAll()
                        .requestMatchers(HttpMethod.GET, "/restricao-alimentar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/documentos-legais/atual").permitAll()
                        .requestMatchers(HttpMethod.POST, "/documentos-legais").permitAll()
                        .requestMatchers(HttpMethod.GET, "/receita/download-image/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/recuperarSenha/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/mudar-senha").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/oauth2/success").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/google/register-user", "/auth/google/register-nutri").permitAll()
                        .requestMatchers(HttpMethod.GET, "/nutricionista/*/publico").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                            request.setAttribute("userGoogle", oauthToken.getPrincipal());
                            request.getRequestDispatcher("/auth/oauth2/success").forward(request, response);
                        })
                        .failureHandler((request, response, exception) -> {
                            String baseUrl = urlFrontend.endsWith("/")
                                    ? urlFrontend.substring(0, urlFrontend.length() - 1)
                                    : urlFrontend;
                            response.sendRedirect(baseUrl + "/login?error=cancelado");
                        }))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}