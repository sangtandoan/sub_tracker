package com.sangtandoan.sub_tracker.token;

import com.sangtandoan.sub_tracker.user.User;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtService implements TokenService {
  private final JwtConfig jwtConfig;

  @Override
  public Token generate(User user) {
    var claims =
        Jwts.claims()
            .add("email", user.getEmail())
            .subject(user.getId().toString())
            .issuedAt(new Date())
            .expiration(
                new Date(System.currentTimeMillis() + 1_000 * this.jwtConfig.getTokenDuration()))
            .build();

    return new Jwt(claims, this.jwtConfig.getSecretKey());
  }

  @Override
  public Optional<Token> parse(String token) {
    try {

      var parser = Jwts.parser().verifyWith(this.jwtConfig.getSecretKey()).build();
      var claims = parser.parseSignedClaims(token).getPayload();

      return Optional.of(new Jwt(claims, this.jwtConfig.getSecretKey()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
