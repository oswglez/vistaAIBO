package com.expectra.roombooking.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.expectra.roombooking.model.User;
import com.expectra.roombooking.service.OAuth0UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final OAuth0JwtValidator jwtValidator;
    private final OAuth0UserService oAuth0UserService;

    public JwtAuthenticationFilter(OAuth0JwtValidator jwtValidator, OAuth0UserService oAuth0UserService) {
        this.jwtValidator = jwtValidator;
        this.oAuth0UserService = oAuth0UserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (StringUtils.hasText(token)) {
                DecodedJWT jwt = jwtValidator.validateToken(token);
                User user = oAuth0UserService.getOrCreateUserFromJwt(jwt);
                
                if (user != null && user.getIsActive()) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("JWT authentication successful for user: {}", user.getUsername());
                } else {
                    log.warn("User not found or inactive for JWT token");
                }
            }
        } catch (JWTVerificationException e) {
            log.warn("JWT verification failed: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 