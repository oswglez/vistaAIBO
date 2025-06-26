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

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        try {
            // Decode the JWT without verification first to get the key ID
            DecodedJWT unverifiedJwt = JWT.decode(token);
            
            // Get the key ID from the JWT header
            String keyId = unverifiedJwt.getKeyId();
            
            // Fetch the public key from Auth0
            JwkProvider provider = new UrlJwkProvider(new URL("https://" + domain + "/.well-known/jwks.json"));
            Jwk jwk = provider.get(keyId);
            RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
            
            // Create the verifier
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build();
            
            // Verify the token
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
} 