package com.sangtandoan.sub_tracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {
  private Class<? extends Enum<?>> enumClass;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    this.enumClass = constraintAnnotation.enumClass();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    Enum<?>[] enumConstants = enumClass.getEnumConstants();
    return Arrays.stream(enumConstants).anyMatch(e -> e.toString().equals(value));
  }
}
