package com.sangtandoan.sub_tracker.subscription;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription, UUID> {}
