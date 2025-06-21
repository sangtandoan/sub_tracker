package com.sangtandoan.sub_tracker.configs;

import com.sangtandoan.sub_tracker.common.SecurityRules;
import com.sangtandoan.sub_tracker.filters.JwtAuthenticationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final List<SecurityRules> securityRules;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(c -> c.configurationSource(this.corsConfigurationSource()))
        .authorizeHttpRequests(
            c -> {

              // Allow all OPTIONS requests (CORS preflight) - should be first
              // If not set this, cors will happend even you have config it
              c.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

              this.securityRules.forEach(rule -> rule.register(c));

              c.anyRequest().authenticated();
            })
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            c -> {
              c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
              c.accessDeniedHandler(
                  (request, response, exception) ->
                      response.setStatus(HttpStatus.FORBIDDEN.value()));
            });

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    var config = new CorsConfiguration();

    // Configure allowed origins
    if ("*".equals(this.frontendUrl)) {
      config.setAllowedOriginPatterns(List.of("*"));
    } else {
      config.setAllowedOrigins(List.of(this.frontendUrl));
    }

    // Configure allowed methods
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

    // Configure allowed headers
    config.setAllowedHeaders(List.of("*"));

    // Allow credentials (for authentication)
    config.setAllowCredentials(true);

    // Configure exposed headers (if your backend sends custom headers)
    config.setExposedHeaders(List.of("Authorization", "Content-Type"));

    // Configure max age for preflight requests (in seconds)
    config.setMaxAge(3600L);

    // Register config to source
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService) {
    var daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());

    return daoAuthenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
