package com.sangtandoan.sub_tracker.subscription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sangtandoan.sub_tracker.exceptions.InvalidEnumException;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public enum SubscriptionDuration {
  DAILY(1, "Daily"),
  MONTHLY(30, "Monthly"),
  SIX_MONTHS(180, "6 Months"),
  WEEKLY(7, "Weekly"),
  YEARLY(365, "Yearly");

  // Getter methods
  private final int days;
  private final String displayName;

  // Constructor
  SubscriptionDuration(int days, String displayName) {
    this.days = days;
    this.displayName = displayName;
  }

  // @JsonValue to make Jackson uses this method to serialize this SubscriptionDuration to json ->
  // serialize to displayName
  @JsonValue
  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }

  // Calculate endDate
  public LocalDate getEndDate(LocalDate startDate) {
    switch (this) {
      case DAILY -> {
        return startDate.plusDays(1);
      }
      case WEEKLY -> {
        return startDate.plusWeeks(1);
      }
      case MONTHLY -> {
        return startDate.plusMonths(1);
      }
      case SIX_MONTHS -> {
        return startDate.plusMonths(6);
      }
      case YEARLY -> {
        return startDate.plusYears(1);
      }
      default -> {
        return startDate.plusDays(days);
      }
    }
  }

  // Static method to find enum by display name (needed for AttributeConverter)
  // @JsonCreator to make Jackson uses this method to deserialize json to SubscriptionDuration ->
  // displayName to SubscriptionDuration
  @JsonCreator
  public static SubscriptionDuration findByDisplayName(String displayName) {
    if (displayName == null) {
      return null;
    }
    displayName = displayName.toLowerCase();

    for (SubscriptionDuration duration : values()) {
      if (duration.getDisplayName().toLowerCase().equals(displayName)) {
        return duration;
      }
    }

    throw new InvalidEnumException(
        "No enum constant with display name: test: " + displayName, SubscriptionDuration.class);
  }
}
