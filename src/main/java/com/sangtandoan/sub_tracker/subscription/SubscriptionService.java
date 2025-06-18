package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.user.UserRepo;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SubscriptionService {
  private final SubscriptionMapper subscriptionMapper;
  private final SubscriptionRepo subscriptionRepo;
  private final UserRepo userRepo;

  public SubscriptionDto create(CreateSubscriptionRequest request) {
    // Adds startDate with Duration to create endDate
    var endDate = request.duration().getEndDate(request.startDate());
    var subscription = this.subscriptionMapper.toEnitity(request);
    subscription.setEndDate(endDate);

    // Take user from security context and set owner of subscription
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var userId = (UUID) authentication.getPrincipal();
    var user = this.userRepo.findById(userId).orElseThrow();
    subscription.setUser(user);

    subscription = this.subscriptionRepo.save(subscription);

    return this.subscriptionMapper.toDto(subscription);
  }
}
