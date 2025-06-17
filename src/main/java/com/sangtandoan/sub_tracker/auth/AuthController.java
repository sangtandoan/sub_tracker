package com.sangtandoan.sub_tracker.auth;

import com.sangtandoan.sub_tracker.token.TokenService;
import com.sangtandoan.sub_tracker.user.UserRepo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final UserRepo userRepo;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> handleLogin(@Valid @RequestBody LoginRequest request) {
    this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    var user = this.userRepo.findByEmail(request.email()).orElseThrow();

    var token = this.tokenService.generate(user);

    return ResponseEntity.ok(new LoginResponse(token.toString()));
  }
}
