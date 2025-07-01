package com.expectra.roombooking.config;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Component
@Slf4j
public class OAuth0JwtValidator {

    @Value("${oauth0.domain}")
    private String domain;

    @Value("${oauth0.audience}")
    private String audience;

    @Value("${oauth0.issuer}")
    private String issuer;

    @Value("${jwt.public.key.path}")
    private String publicKeyPath;

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        try {
            DecodedJWT unverifiedJwt = JWT.decode(token);
            String keyId = unverifiedJwt.getKeyId();

            Algorithm algorithm;
            JWTVerifier verifier;

            if ("dev-key-1".equals(keyId)) {
                // Token de pruebas: usa tu clave pública local
                RSAPublicKey publicKey = getLocalPublicKey();
                algorithm = Algorithm.RSA256(publicKey, null);
                verifier = JWT.require(algorithm).build();
            } else {
                // Token de Auth0: usa JWKS
                JwkProvider provider = new UrlJwkProvider(new URL("https://" + domain + "/.well-known/jwks.json"));
                Jwk jwk = provider.get(keyId);
                RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
                algorithm = Algorithm.RSA256(publicKey, null);
                verifier = JWT.require(algorithm)
                        .withIssuer(issuer)
                        .withAudience(audience)
                        .build();
            }

            return verifier.verify(token);

        } catch (Exception e) {
            log.error("Error validating JWT token: {}", e.getMessage());
            throw new JWTVerificationException("Invalid token");
        }
    }

    public String extractUserId(String token) {
        try {
            DecodedJWT jwt = validateToken(token);
            return jwt.getSubject();
        } catch (Exception e) {
            log.error("Error extracting user ID from token: {}", e.getMessage());
            return null;
        }
    }

    public Map<String, Claim> extractClaims(String token) {
        try {
            DecodedJWT jwt = validateToken(token);
            return jwt.getClaims();
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }

    private RSAPublicKey getLocalPublicKey() throws Exception {
        String key = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(publicKeyPath)))
            .replaceAll("-----BEGIN PUBLIC KEY-----", "")
            .replaceAll("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        java.security.spec.X509EncodedKeySpec spec = new java.security.spec.X509EncodedKeySpec(keyBytes);
        java.security.KeyFactory kf = java.security.KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }
} 