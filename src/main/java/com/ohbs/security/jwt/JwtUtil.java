package com.ohbs.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ohbs.common.exception.InvalidTokenException;
import com.ohbs.common.model.User;
import com.ohbs.common.repository.UserRepository;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component("managerJwtUtil") // Specify a unique bean name to avoid conflict
@Slf4j
public class JwtUtil {

    private final Key key;
    private final int accessTokenExpirationMs;
    private final int refreshTokenExpirationMs;
    private final UserRepository userRepository;

    public JwtUtil(
            UserRepository userRepository,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMs}") int accessTokenExpirationMs,
            @Value("${jwt.refreshExpirationMs}") int refreshTokenExpirationMs
    ) {
        this.userRepository = userRepository;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public Long getUserIdFromEmail(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email))
                .getId();
    }

    // Generate access token
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Generate refresh token
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Extract email from token
    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // Extract role from token
    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("Unsupported JWT: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Malformed JWT: " + e.getMessage());
        } catch (SecurityException e) {
            throw new InvalidTokenException("Invalid JWT signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("Empty JWT claims: " + e.getMessage());
        } catch (JwtException e) {
            throw new InvalidTokenException("JWT error: " + e.getMessage());
        }
    }

    // Extract all claims from token
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Refresh access token using refresh token (if implemented in your case)
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new JwtException("Invalid or expired refresh token.");
        }

        String username = getEmailFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return generateAccessToken(user);
    }
}
