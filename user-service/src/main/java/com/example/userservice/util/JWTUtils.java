package com.example.userservice.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JWTUtils {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7; // 7 days

    private final SecretKey Key;

    public JWTUtils() {
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        // FIX: Do not use Base64 decoding on a plain string secret. Use raw bytes.
        byte[] keyBytes = secreteString.getBytes(StandardCharsets.UTF_8);
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, Key)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            String username = extractClaims(token, Claims::getSubject);
            logger.info("[JWTUtils] Extracted username: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("[JWTUtils] Failed to extract username: {}", e.getMessage());
            return null;
        }
    }


    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        try {
            // FIX: Use the older Jwts.parser() method which is compatible with v0.9.1
            Claims claims = Jwts.parser()
                    .setSigningKey(Key)
                    .parseClaimsJws(token)
                    .getBody();
            return claimsTFunction.apply(claims);
        } catch (Exception e) {
            logger.error("[JWTUtils] Failed to parse claims for token: {}", token, e);
            throw e; // Re-throw to be handled by the calling method
        }
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        try {
            logger.info("[JWTUtils] Validating token: {}", token);
            final String username = extractUsername(token);
            logger.info("[JWTUtils] Username from token: {}", username);
            boolean valid = (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            logger.info("[JWTUtils] Token valid: {}", valid);
            return valid;
        } catch (Exception e) {
            logger.error("[JWTUtils] Token validation error: {}", e.getMessage());
            return false;
        }
    }


    private boolean isTokenExpired(String token) {
        try {
            return extractClaims(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            // If claims cannot be extracted, the token is invalid, hence considered "expired".
            logger.error("[JWTUtils] Could not determine expiration, token is invalid.");
            return true;
        }
    }
}
