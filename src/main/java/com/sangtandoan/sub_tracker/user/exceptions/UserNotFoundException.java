package com.sangtandoan.sub_tracker.user.exceptions;

import com.sangtandoan.sub_tracker.exceptions.BaseAppException;
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
