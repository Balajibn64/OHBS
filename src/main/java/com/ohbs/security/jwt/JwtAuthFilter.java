package com.ohbs.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    String authHeader = request.getHeader("Authorization");

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        String token = authHeader.substring(7);

	        try {
	            // ✅ validateToken may throw ExpiredJwtException or other exceptions
	            if (!jwtUtil.validateToken(token)) {
	                throw new RuntimeException("Invalid JWT token");
	            }

	            String username = jwtUtil.getEmailFromToken(token);
	            Claims claims = jwtUtil.getAllClaimsFromToken(token);
	            String role = claims.get("role", String.class);

	            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

	            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                    username, null, authorities);

	            SecurityContextHolder.getContext().setAuthentication(authentication);

	        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.setContentType("application/json");
	            response.getWriter().write("{\"error\": \"Token has expired. Please log in again.\"}");
	            return;
	        } catch (io.jsonwebtoken.SignatureException | io.jsonwebtoken.MalformedJwtException ex) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.setContentType("application/json");
	            response.getWriter().write("{\"error\": \"Invalid JWT token signature.\"}");
	            return;
	        } catch (Exception ex) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.setContentType("application/json");
	            response.getWriter().write("{\"error\": \"Invalid or expired JWT token.\"}");
	            return;
	        }
	    }

	    filterChain.doFilter(request, response);
	}


}
