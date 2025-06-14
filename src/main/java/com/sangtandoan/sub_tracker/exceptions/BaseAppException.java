package com.sangtandoan.sub_tracker.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseAppException extends RuntimeException {
  public BaseAppException(String message) {
    super(message);
  }

  public abstract HttpStatus getHttpStatus();
}
