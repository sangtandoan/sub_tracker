package com.sangtandoan.sub_tracker.token;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token.jwt")
@Data
public class JwtConfig {
  private long tokenDuration;
  private String secretKey;

  public SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(this.secretKey.getBytes());
  }
}
