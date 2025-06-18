package com.sangtandoan.sub_tracker.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidEnumException extends BaseAppException {
  private final Class<? extends Enum<?>> enumClass;

  public InvalidEnumException(String message, Class<? extends Enum<?>> enumClass) {
    super(message);
    this.enumClass = enumClass;
  }

  public Class<? extends Enum<?>> getEnumClass() {
    return enumClass;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
