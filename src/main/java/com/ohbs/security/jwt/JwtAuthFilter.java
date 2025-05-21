package com.ohbs.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ohbs.common.exception.InvalidTokenException;
import com.ohbs.common.exception.MissingTokenException;
import com.ohbs.common.exception.UnauthorizedException;

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
	
	// Method to get required role for a specific endpoint
    private String getRequiredRoleForEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        
        if (uri.startsWith("/admin")) {
            return "ADMIN";
        } else if (uri.startsWith("/manager")) {
            return "MANAGER";
        } else if (uri.startsWith("/customer")) {
            return "CUSTOMER";
        }
        
        return null;  // No role required, or return a default role if needed
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			
			//Filter api which doesn't requires login				
			String requestURI = request.getRequestURI();
	        if (requestURI.startsWith("/auth/") || requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
	            filterChain.doFilter(request, response);
	            return;
	        }
			
			String authHeader = request.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				throw new MissingTokenException("Token is missing or improperly formatted.");
			}

			String token = authHeader.substring(7);

			if (jwtUtil.validateToken(token)) {
				String username = jwtUtil.getEmailFromToken(token);
				Claims claims = jwtUtil.getAllClaimsFromToken(token);
				String role = jwtUtil.getRoleFromToken(token);
				
				// Get the required role for the current endpoint
                String requiredRole = getRequiredRoleForEndpoint(request);

                // Check if the user's role matches the required role for the endpoint
                if (requiredRole != null && !requiredRole.equals(role)) {
                    throw new UnauthorizedException("Access Denied: Insufficient role.");
                }
				

				List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						null, authorities);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			// Continue filter chain if no issues
			filterChain.doFilter(request, response);

		} catch (MissingTokenException | InvalidTokenException | UnauthorizedException e) {
			// Handle InvalidTokenException manually since filters can't propagate it
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			String json = String.format("{\"status\": 401, \"message\": \"%s\", \"path\": \"%s\"}", e.getMessage(),
					request.getRequestURI());

			try (PrintWriter out = response.getWriter()) {
				out.print(json);
				out.flush(); // Ensure the response is flushed
			}
		}
	}


}
