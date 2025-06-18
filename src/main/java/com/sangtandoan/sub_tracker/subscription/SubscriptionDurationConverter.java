package com.sangtandoan.sub_tracker.subscription;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Using this to convert Java Class to DB
@Converter(autoApply = true)
public class SubscriptionDurationConverter
    implements AttributeConverter<SubscriptionDuration, String> {

  @Override
  public String convertToDatabaseColumn(SubscriptionDuration attribute) {
    return (attribute == null) ? null : attribute.getDisplayName();
  }

  @Override
  public SubscriptionDuration convertToEntityAttribute(String dbData) {
    return (dbData == null) ? null : SubscriptionDuration.findByDisplayName(dbData);
  }
}
