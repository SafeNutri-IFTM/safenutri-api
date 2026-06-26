package com.safenutri.safenutri_api.core.security.rateLimit;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.safenutri.safenutri_api.core.security.jwt.UserPrincipal;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimitingService rateLimitingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String rateLimitKey = determineKey(request);

        Bucket bucket = rateLimitingService.resolveBucket(rateLimitKey);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            long secondsToWait = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(secondsToWait));

            String jsonError = String.format("{\"error\": \"Muitas requisições. Tente novamente em %d segundos.\"}", secondsToWait);
            response.getWriter().write(jsonError);
        }
    }

    private String determineKey(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Object principal = auth.getPrincipal();

            if (principal instanceof UserPrincipal) {
                Long userId = ((UserPrincipal) principal).getId();
                return "USER-" + userId;
            }

            return "USER-" + auth.getName();
        }

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return "IP-" + xForwardedFor.split(",")[0].trim();
        }

        return "IP-" + request.getRemoteAddr();
    }

}
