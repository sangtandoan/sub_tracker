package com.sangtandoan.sub_tracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseAppException.class)
  public ResponseEntity<AppError> handleBaseAppException(BaseAppException e) {
    var error = new AppError();
    var statusCode = e.getHttpStatus().value();

    error.setStatusCode(e.getHttpStatus().value());
    error.setError(e.getMessage());

    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<AppError> handleRuntimeException(RuntimeException e) {
    System.out.println(e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
