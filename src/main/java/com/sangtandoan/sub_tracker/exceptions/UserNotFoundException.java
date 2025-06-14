package com.sangtandoan.sub_tracker.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseAppException {
  public UserNotFoundException() {
    super("User not found!");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
