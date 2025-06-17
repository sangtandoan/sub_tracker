package com.sangtandoan.sub_tracker.oauth;

import com.sangtandoan.sub_tracker.common.SecurityRules;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class OAuthSecurityRules implements SecurityRules {

  @Override
  public void register(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          registry) {
    registry.requestMatchers("/api/v1/oauth/**").permitAll();
  }
}
