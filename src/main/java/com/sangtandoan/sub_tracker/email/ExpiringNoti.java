package com.sangtandoan.sub_tracker.email;

public record ExpiringNoti(
    String userName, String subscriptionName, int expiringInDays, String to) {}
