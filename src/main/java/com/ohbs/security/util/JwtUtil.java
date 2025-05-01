package com.ohbs.security.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ohbs.models.User;
import com.ohbs.repository.UserRepository;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

    private final Key key;
    private final int accessTokenExpirationMs;
//    private final int refreshTokenExpirationMs;

//    private final UserRepository userRepository;

    public JwtUtil(
            UserRepository userRepository,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMs}") int accessTokenExpirationMs,
            @Value("${jwt.refreshExpirationMs}") int refreshTokenExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirationMs = accessTokenExpirationMs;
//        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
//        this.userRepository = userRepository;
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

//    public String generateRefreshToken(User user) {
//        return Jwts.builder()
//                .setSubject(user.getUsername())
//                .claim("role", user.getRole().name())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }

    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Empty JWT claims: {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("JWT error: {}", e.getMessage());
        }
        return false;
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

//    public String refreshAccessToken(String refreshToken) {
//        if (!validateToken(refreshToken)) {
//            throw new JwtException("Invalid or expired refresh token.");
//        }
//
//        String username = getEmailFromToken(refreshToken);
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        return generateAccessToken(user);
//    }
}
