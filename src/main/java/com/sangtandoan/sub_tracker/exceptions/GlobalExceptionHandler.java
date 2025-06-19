package com.sangtandoan.sub_tracker.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

  @ExceptionHandler(InvalidEnumException.class)
  public ResponseEntity<AppError> handleInvalidEnumException(InvalidEnumException e) {
    var err = new AppError();
    var statusCode = e.getHttpStatus();
    var enumClass = e.getEnumClass();

    err.setStatusCode(statusCode.value());

    List<String> validValues =
        Arrays.stream(enumClass.getEnumConstants())
            .map(Enum::toString)
            .collect(Collectors.toList());

    err.setError(Map.of("message", e.getMessage(), "validValues", validValues));

    return ResponseEntity.status(statusCode.value()).body(err);
  }

  @ExceptionHandler()
  public ResponseEntity<AppError> handleAccessDeniedException(AccessDeniedException e) {
    var err = new AppError();
    var statusCode = HttpStatus.FORBIDDEN.value();

    err.setStatusCode(statusCode);
    err.setError(e.getMessage());

    return ResponseEntity.status(statusCode).body(err);
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<AppError> handleHttpMessageNotReadableException(RuntimeException e) {
    if (e instanceof HttpMessageNotReadableException) {
      var ex = (HttpMessageNotReadableException) e;
      // Get the root cause of the exception
      var rootCause = ex.getRootCause();

      // Check if the root cause is instance of InvalidEnumException
      if (rootCause instanceof InvalidEnumException) {
        return this.handleInvalidEnumException((InvalidEnumException) rootCause);
      }
    }

    var error = new AppError();
    var statusCode = HttpStatus.BAD_REQUEST.value();

    error.setStatusCode(statusCode);
    error.setError("Invalid request format: " + e.getMessage());

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
    log.info(e.getClass().toString());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
