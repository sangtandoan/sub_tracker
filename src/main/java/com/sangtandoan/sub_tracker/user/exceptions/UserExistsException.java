package com.sangtandoan.sub_tracker.user.exceptions;

import com.sangtandoan.sub_tracker.exceptions.BaseAppException;
import org.springframework.http.HttpStatus;

public class UserExistsException extends BaseAppException {
  public UserExistsException() {
    super("User has already existed!");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
