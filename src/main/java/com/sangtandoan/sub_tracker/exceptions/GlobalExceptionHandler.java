package com.sangtandoan.sub_tracker.exceptions;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseAppException.class)
  public ResponseEntity<AppError> handleBaseAppException(BaseAppException e) {
    var error = new AppError();
    var statusCode = e.getHttpStatus().value();

    error.setStatusCode(e.getHttpStatus().value());
    error.setError(e.getMessage());

    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<AppError> handleValidationException(MethodArgumentNotValidException e) {
    var error = new AppError();
    error.setStatusCode(HttpStatus.BAD_REQUEST.value());

    Map<String, String> map = new HashMap<>();
    e.getBindingResult()
        .getFieldErrors()
        .forEach(err -> map.put(err.getField(), err.getDefaultMessage()));

    error.setError(map);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<AppError> handleAccessDeniedException(AuthenticationException e) {
    var httpStatus = HttpStatus.BAD_REQUEST;

    return ResponseEntity.status(httpStatus)
        .body(new AppError(httpStatus.value(), "Email or Password is wrong"));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<AppError> handleRuntimeException(RuntimeException e) {

    log.error(e.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
