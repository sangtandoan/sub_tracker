package com.sangtandoan.sub_tracker.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Jwt implements Token {
  private final Claims claims;
  private final SecretKey secretKey;

  @Override
  public boolean isValid() {
    return this.claims.getExpiration().after(new Date());
  }

  @Override
  public UUID getUserId() {
    return UUID.fromString(this.claims.getSubject());
  }

  @Override
  public String toString() {
    return Jwts.builder().claims(this.claims).signWith(this.secretKey).compact();
  }
}
