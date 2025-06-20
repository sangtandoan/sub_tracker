package com.sangtandoan.sub_tracker.oauth;

import com.sangtandoan.sub_tracker.auth.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {
  private final OAuthService oAuthService;

  @PostMapping("/url")
  public OAuthUrlResponse generateAuthUrl(@RequestBody OAuthUrlRequest request) {
    return this.oAuthService.generateAuthUrl(request);
  }

  @GetMapping("/google/callback")
  // public LoginResponse handleCallback(@RequestParam String code, @RequestParam String state) {
  public LoginResponse handleCallback(@RequestParam String code, @RequestParam String redirectUrl) {

    return this.oAuthService.handleGoogleCallback(code, redirectUrl);
  }
}
