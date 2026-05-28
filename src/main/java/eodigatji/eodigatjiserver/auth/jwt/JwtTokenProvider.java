package eodigatji.eodigatjiserver.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String USER_ID_CLAIM = "userId";
    private static final String EMAIL_CLAIM = "email";
    private static final String TYPE_CLAIM = "type";
    private static final String ACCESS_TYPE = "access";
    private static final String REFRESH_TYPE = "refresh";
    private static final int MIN_SECRET_LENGTH = 32;

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs
    ) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_LENGTH) {
            throw new IllegalArgumentException("JWT_SECRET must be at least 32 bytes.");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String createAccessToken(Long userId, String email) {
        return createToken(userId, email, ACCESS_TYPE, accessTokenExpirationMs);
    }

    public String createRefreshToken(Long userId, String email) {
        return createToken(userId, email, REFRESH_TYPE, refreshTokenExpirationMs);
    }

    public boolean isValidRefreshToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return REFRESH_TYPE.equals(claims.get(TYPE_CLAIM, String.class));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserIdFromRefreshToken(String token) {
        Claims claims = parseClaims(token);
        validateTokenType(claims, REFRESH_TYPE);
        return claims.get(USER_ID_CLAIM, Long.class);
    }

    public LocalDateTime getRefreshTokenExpiresAt(String token) {
        Claims claims = parseClaims(token);
        validateTokenType(claims, REFRESH_TYPE);
        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }

    private String createToken(Long userId, String email, String type, long expirationMs) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(expirationMs);

        return Jwts.builder()
                .claims(Map.of(
                        USER_ID_CLAIM, userId,
                        EMAIL_CLAIM, email,
                        TYPE_CLAIM, type
                ))
                .subject(String.valueOf(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void validateTokenType(Claims claims, String expectedType) {
        if (!expectedType.equals(claims.get(TYPE_CLAIM, String.class))) {
            throw new JwtException("Invalid token type.");
        }
    }
}
