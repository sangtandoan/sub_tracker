package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.user.User;
import com.sangtandoan.sub_tracker.user.UserRepo;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SubscriptionService {
  private final SubscriptionMapper subscriptionMapper;
  private final SubscriptionRepo subscriptionRepo;
  private final UserRepo userRepo;

  public List<SubscriptionDto> findAll() {
    var user = this.getUserFromContext();

    var subscriptions = this.subscriptionRepo.findAllByUser(user);

    return subscriptions.stream().map(subscriptionMapper::toDto).toList();
  }

  public SubscriptionDto findById(UUID id) {
    var subscription =
        this.subscriptionRepo.findById(id).orElseThrow(SubscriptionNotFoundException::new);

    var user = this.getUserFromContext();
    if (!user.equals(subscription.getUser()))
      throw new AccessDeniedException("Resources not belong to user.");

    return this.subscriptionMapper.toDto(subscription);
  }

  public SubscriptionDto create(CreateSubscriptionRequest request) {
    // Adds startDate with Duration to create endDate
    var subscription = this.subscriptionMapper.toEnitity(request);
    var endDate = request.duration().getEndDate(request.startDate());
    subscription.setEndDate(endDate);

    // Take user from security context and set owner of subscription
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var userId = (UUID) authentication.getPrincipal();
    var user = this.userRepo.findById(userId).orElseThrow();
    subscription.setUser(user);

    this.subscriptionRepo.save(subscription);

    return this.subscriptionMapper.toDto(subscription);
  }

  public SubscriptionDto partialUpdate(UUID id, UpdateSubscriptionRequest request) {
    var subscription =
        this.subscriptionRepo.findById(id).orElseThrow(SubscriptionNotFoundException::new);

    this.subscriptionMapper.partialUpdate(request, subscription);
    subscription.setEndDate();

    this.subscriptionRepo.save(subscription);

    return this.subscriptionMapper.toDto(subscription);
  }

  public void delete(UUID id) {
    this.subscriptionRepo.deleteById(id);
  }

  private User getUserFromContext() {
    var userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return this.userRepo.findById(userId).orElseThrow();
  }
}
