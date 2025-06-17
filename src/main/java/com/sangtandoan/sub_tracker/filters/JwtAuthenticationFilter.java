package com.sangtandoan.sub_tracker.filters;

import com.sangtandoan.sub_tracker.token.TokenService;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final TokenService tokenService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Checks if apply this filter or not
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      // pass not apply this filter
      filterChain.doFilter(request, response);
      return;
    }

    // Decides to reject or authenticate request
    var tokenString = authHeader.replace("Bearer ", "");

    var token = this.tokenService.parse(tokenString).orElse(null);
    // Reject
    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    var authentication =
        new UsernamePasswordAuthenticationToken(token.getUserId(), null, Collections.emptyList());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
