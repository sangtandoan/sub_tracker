package com.sangtandoan.sub_tracker.oauth;

import com.sangtandoan.sub_tracker.exceptions.BaseAppException;
import org.springframework.http.HttpStatus;

public class OAuthAuthenticationException extends BaseAppException {
  public OAuthAuthenticationException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
