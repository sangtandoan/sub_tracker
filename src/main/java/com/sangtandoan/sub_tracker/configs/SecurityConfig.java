package com.sangtandoan.sub_tracker.configs;

import com.sangtandoan.sub_tracker.common.SecurityRules;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final List<SecurityRules> securityRules;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(c -> c.configurationSource(this.corsConfigurationSource()))
        .authorizeHttpRequests(
            c -> {
              this.securityRules.forEach(rule -> rule.register(c));

              c.anyRequest().authenticated();
            })
        .exceptionHandling(
            c -> {
              c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
              c.accessDeniedHandler(
                  (request, response, exeception) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                  });
            });

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    var config = new CorsConfiguration();

    // config for cors
    config.setAllowedOriginPatterns(List.of("*"));

    // register config to source
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }
}
