package com.sangtandoan.sub_tracker.subscription;

import java.time.LocalDate;

public record UpdateSubscriptionRequest(
    String name, LocalDate startDate, SubscriptionDuration duration) {}
