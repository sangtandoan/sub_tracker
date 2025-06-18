package com.sangtandoan.sub_tracker.subscription;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  Subscription toEnitity(CreateSubscriptionRequest request);

  SubscriptionDto toDto(Subscription subscription);
}
