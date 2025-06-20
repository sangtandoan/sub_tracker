package com.sangtandoan.sub_tracker.common.filter;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FilterExceptionHandler {

  @ExceptionHandler(FilterValidationException.class)
  public ResponseEntity<Map<String, Object>> handleFilterValidationException(
      FilterValidationException ex) {
    Map<String, Object> errorResponse =
        Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "error", "Bad Request",
            "message", ex.getMessage(),
            "type", "Filter Validation Error");

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    // Only handle filter-related IllegalArgumentExceptions
    if (ex.getMessage() != null
            && (ex.getMessage().contains("Invalid filter operation")
                || ex.getMessage().contains("Invalid date format")
                || ex.getMessage().contains("Invalid datetime format"))
        || ex.getMessage().contains("Invalid enum constant")) {
      Map<String, Object> errorResponse =
          Map.of(
              "timestamp", LocalDateTime.now(),
              "status", HttpStatus.BAD_REQUEST.value(),
              "error", "Bad Request",
              "message", ex.getMessage(),
              "type", "Invalid Filter Value");

      return ResponseEntity.badRequest().body(errorResponse);
    }

    // Re-throw if not filter-related
    throw ex;
  }
}
