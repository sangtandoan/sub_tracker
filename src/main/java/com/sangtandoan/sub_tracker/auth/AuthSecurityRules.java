package com.sangtandoan.sub_tracker.auth;

import com.sangtandoan.sub_tracker.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AuthSecurityRules implements SecurityRules {

  @Override
  public void register(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          registry) {
    registry.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll();
  }
}
