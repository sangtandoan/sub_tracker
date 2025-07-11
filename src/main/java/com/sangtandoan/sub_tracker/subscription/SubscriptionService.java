package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.common.filter.FilterParser;
import com.sangtandoan.sub_tracker.common.filter.SpecificationBuilder;
import com.sangtandoan.sub_tracker.user.User;
import com.sangtandoan.sub_tracker.user.UserRepo;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SubscriptionService {
  private final SubscriptionMapper subscriptionMapper;
  private final SubscriptionRepo subscriptionRepo;
  private final UserRepo userRepo;

  public Page<SubscriptionDto> findAll(Pageable pageable, Boolean isCancelled) {
    var user = this.getUserFromContext();

    // var subscriptions = this.subscriptionRepo.findAllByUser(user, pageable);
    Specification<Subscription> spec =
        Specification.allOf(
            SubscriptionSpecifications.belongsToUser(user)
                .and(SubscriptionSpecifications.isCancelled(isCancelled)));

    var subscriptions = this.subscriptionRepo.findAll(spec, pageable);

    return subscriptions.map(subscriptionMapper::toDto);
  }

  public Page<SubscriptionDto> findAllWithFilters(
      Pageable pageable, Boolean isCancelled, Map<String, String[]> queryParams) {
    var user = this.getUserFromContext();

    // Parse filter criteria from query parameters
    var filterCriteria = FilterParser.parseFilters(queryParams);

    // Build base specification with user ownership and cancellation status
    Specification<Subscription> spec =
        Specification.allOf(
            SubscriptionSpecifications.belongsToUser(user)
                .and(SubscriptionSpecifications.isCancelled(isCancelled)));

    // Add dynamic filters if present
    if (!filterCriteria.isEmpty()) {
      Specification<Subscription> filterSpec =
          SpecificationBuilder.buildSpecification(filterCriteria);
      spec = spec.and(filterSpec);
    }

    var subscriptions = this.subscriptionRepo.findAll(spec, pageable);

    return subscriptions.map(subscriptionMapper::toDto);
  }

  public Page<SubscriptionDto> search(String searchTerm, Pageable pageable, Boolean isCancelled) {
    var user = this.getUserFromContext();

    // var subscriptions = this.subscriptionRepo.searchByName(user.getId(), searchTerm, pageable);

    Specification<Subscription> spec =
        Specification.allOf(
            SubscriptionSpecifications.belongsToUser(user)
                .and(SubscriptionSpecifications.isCancelled(isCancelled))
                .and(SubscriptionSpecifications.search(searchTerm, pageable)));

    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

    var subscriptions = this.subscriptionRepo.findAll(spec, pageable);

    return subscriptions.map(subscriptionMapper::toDto);
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
    var subscription = this.subscriptionMapper.toEntity(request);
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

  public List<Subscription> findExpiringInDays(int days) {
    Specification<Subscription> spec =
        Specification.allOf(
            SubscriptionSpecifications.isCancelled(false)
                .and(SubscriptionSpecifications.expiringInDays(days)));

    return this.subscriptionRepo.findAll(spec);
  }

  private User getUserFromContext() {
    var userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return this.userRepo.findById(userId).orElseThrow();
  }
}
