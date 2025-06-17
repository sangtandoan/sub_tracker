package com.sangtandoan.sub_tracker.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sangtandoan.sub_tracker.auth.LoginResponse;
import com.sangtandoan.sub_tracker.token.TokenService;
import com.sangtandoan.sub_tracker.user.User;
import com.sangtandoan.sub_tracker.user.UserProvider;
import com.sangtandoan.sub_tracker.user.UserRepo;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@Service
public class OAuthService {
  // TODO: random state for each request and stores in valkey
  private static final String STATE = "secret_state";

  private final OAuthProviderRegistry registry;
  private final RestTemplate restTemplate;
  private final UserRepo userRepo;
  private final TokenService tokenService;

  public OAuthUrlResponse generateAuthUrl(OAuthUrlRequest request) {

    var authUrl = this.registry.generateAuthUrl(request.provider(), request.redirectUrl(), STATE);

    return new OAuthUrlResponse(authUrl, STATE);
  }

  public LoginResponse handleGoogleCallback(String code, String state, String redirectUrl) {
    // Validates state
    // TODO: change to valkey to check
    if (!STATE.equals(state)) throw new SecurityException("Invalid state parameter");

    // TODO: clear state

    try {

      // Exchange code for access token
      var clientConfig = this.registry.getClientConfig(OAuthProvider.GOOGLE);
      var accessToken = this.exchangeForAccessToken(code, redirectUrl, clientConfig);

      // Get user info from google
      var googleUserInfoUrl = this.registry.getUserInfoUrl(OAuthProvider.GOOGLE);

      var userInfoResponse = this.getUserInfoResponse(googleUserInfoUrl, accessToken);
      if (!userInfoResponse.getStatusCode().is2xxSuccessful()
          || userInfoResponse.getBody() == null) {
        throw new Exception();
      }

      GoogleUserInfo userInfo = userInfoResponse.getBody();
      System.out.println(userInfo);

      // Handle user and auth provider in db
      User user = this.findOrCreateUser(userInfo);

      // Generate jwt tokens
      var userToken = this.tokenService.generate(user);

      return new LoginResponse(userToken.toString());

    } catch (Exception e) {

      log.error(e.getMessage());

      throw new OAuthAuthenticationException("Failed to authenticate with Google");
    }
  }

  @Transactional
  private User findOrCreateUser(GoogleUserInfo userInfo) {
    var user = this.userRepo.findByEmail(userInfo.getEmail()).orElse(null);
    if (user == null) {
      user = new User();
      user.setEmail(userInfo.getEmail());
      user.setName(userInfo.getName());
      user.setEmailVerified(true);
      this.userRepo.save(user);
    }

    UserProvider provider =
        user.getProviders().stream()
            .filter(u -> u.getProviderName().equals(OAuthProvider.GOOGLE))
            .findFirst()
            .orElse(null);

    if (provider == null) {
      provider = new UserProvider();
      provider.setProviderName(OAuthProvider.GOOGLE);
      provider.setProviderUserId(userInfo.getId());
      provider.setUser(user);
      user.getProviders().add(provider);
    }

    return user;
  }

  private ResponseEntity<GoogleUserInfo> getUserInfoResponse(String uri, String accessToken) {
    // Add authorization header
    var headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    var request = new HttpEntity<GoogleUserInfo>(headers);

    // Execute request
    return this.restTemplate.exchange(uri, HttpMethod.GET, request, GoogleUserInfo.class);
  }

  private String exchangeForAccessToken(String code, String redirectUrl, OAuthConfig clientConfig)
      throws IOException {
    return new GoogleAuthorizationCodeTokenRequest(
            new NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            clientConfig.getClientId(),
            clientConfig.getClientSecret(),
            code,
            redirectUrl)
        .execute()
        .getAccessToken();
  }
}
