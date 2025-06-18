package com.sangtandoan.sub_tracker.subscription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateSubscriptionRequest(
    @NotBlank String name, @NotNull LocalDate startDate, @NotNull SubscriptionDuration duration) {}
