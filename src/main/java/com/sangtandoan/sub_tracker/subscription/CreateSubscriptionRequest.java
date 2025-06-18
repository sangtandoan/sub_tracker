package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateSubscriptionRequest(
    @NotBlank String name,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    @NotNull @ValidEnum(message = "Invalid enum value.", enumClass = SubscriptionDuration.class)
        String duration) {

  public SubscriptionDuration getDuration() {
    return SubscriptionDuration.valueOf(duration);
  }
}
