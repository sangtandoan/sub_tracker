package com.sangtandoan.sub_tracker.exceptions;

import lombok.Data;

@Data
public class AppError {

  private int statusCode;
  private String error;
}
