package com.sangtandoan.sub_tracker.oauth;

import java.util.Map;
import java.util.Optional;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OAuthProviderRegistry {
  private final Map<OAuthProvider, ClientRegistration> providers;
  private final Map<OAuthProvider, Map<String, String>> specificParams;
  private final Map<OAuthProvider, OAuthConfig> providerConfigs;

  public OAuthProviderRegistry(GoogleOAuthConfig googleOAuthConfig) {
    this.providers =
        Map.of(
            OAuthProvider.GOOGLE,
            CommonOAuth2Provider.GOOGLE
                .getBuilder("google")
                .clientId(googleOAuthConfig.getClientId())
                .clientSecret(googleOAuthConfig.getClientSecret())
                .build());

    this.specificParams =
        Map.of(
            OAuthProvider.GOOGLE,
            Map.of(
                "access_type", "offline",
                "prompt", "consent"));

    this.providerConfigs = Map.of(OAuthProvider.GOOGLE, googleOAuthConfig);
  }

  public String generateAuthUrl(OAuthProvider provider, String redirectUri, String state) {
    var registration = this.providers.get(provider);
    if (registration == null) {
      throw new IllegalArgumentException("Unsupported provider: " + provider.toString());
    }

    var builder =
        UriComponentsBuilder.fromUriString(registration.getProviderDetails().getAuthorizationUri())
            .queryParam("client_id", registration.getClientId())
            .queryParam("redirect_uri", redirectUri)
            .queryParam("state", state)
            .queryParam("response_type", "code")
            .queryParam("scope", String.join(" ", registration.getScopes()));

    var params = this.specificParams.get(provider);
    if (params != null) {
      params.forEach(builder::queryParam);
    }

    return builder.toUriString();
  }

  public Optional<OAuthConfig> getClientConfig(OAuthProvider provider) {
    var config = this.providerConfigs.get(provider);
    if (config == null) {
      return Optional.empty();
    }

    return Optional.of(config);
  }

  public Optional<String> getUserInfoUri(OAuthProvider provider) {
    var uri = this.providers.get(provider).getProviderDetails().getUserInfoEndpoint().getUri();
    if (uri == null) {
      return Optional.empty();
    }

    return Optional.of(uri);
  }
}
