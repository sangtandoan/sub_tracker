package com.sangtandoan.sub_tracker.subscription;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  Subscription toEnitity(CreateSubscriptionRequest request);

  SubscriptionDto toDto(Subscription subscription);

  // nullValuePropertyMappingStrategy is used for when property is null
  // nullValueMappingStrategy is used for when entire object is null
  @BeanMapping(
      unmappedTargetPolicy = ReportingPolicy.IGNORE,
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdate(UpdateSubscriptionRequest request, @MappingTarget Subscription subscription);
}
