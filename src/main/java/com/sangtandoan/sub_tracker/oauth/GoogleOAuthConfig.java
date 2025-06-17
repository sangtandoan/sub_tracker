package com.sangtandoan.sub_tracker.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class GoogleOAuthConfig implements OAuthConfig {

  private String clientId;
  private String clientSecret;
}
