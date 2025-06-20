package com.sangtandoan.sub_tracker.subscription;

import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionDto(
    UUID id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    SubscriptionDuration duration,
    boolean isCancelled) {}
