package com.sangtandoan.sub_tracker.common.filter;

import com.sangtandoan.sub_tracker.exceptions.InvalidEnumException;
import com.sangtandoan.sub_tracker.subscription.SubscriptionDuration;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder {

  public static <T> Specification<T> buildSpecification(List<FilterCriteria> criteria) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      for (FilterCriteria criterion : criteria) {
        Predicate predicate = buildPredicate(criterion, root, cb);
        if (predicate != null) {
          predicates.add(predicate);
        }
      }

      // - Converts the List to an array of Predicate objects
      // - `new Predicate[0]` is a modern Java idiom - it creates an empty array as a "hint" for the
      // type
      // - The JVM will create an appropriately sized array internally
      // - This is more efficient than `new Predicate[predicates.size()]`
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  @SuppressWarnings("unchecked")
  private static <T> Predicate buildPredicate(
      FilterCriteria criteria, Root<T> root, CriteriaBuilder cb) {
    Path<Object> path = getPath(root, criteria.field());
    Object value = convertValue(criteria.value(), path.getJavaType());

    return switch (criteria.operation()) {
      case EQUALS -> cb.equal(path, value);
      case NOT_EQUALS -> cb.notEqual(path, value);
      case GREATER_THAN -> {
        @SuppressWarnings("rawtypes")
        Path comparablePath = path;
        yield cb.greaterThan(comparablePath, (Comparable) value);
      }
      case GREATER_THAN_OR_EQUAL -> {
        @SuppressWarnings("rawtypes")
        Path comparablePath = path;
        yield cb.greaterThanOrEqualTo(comparablePath, (Comparable) value);
      }
      case LESS_THAN -> {
        @SuppressWarnings("rawtypes")
        Path comparablePath = path;
        yield cb.lessThan(comparablePath, (Comparable) value);
      }
      case LESS_THAN_OR_EQUAL -> {
        @SuppressWarnings("rawtypes")
        Path comparablePath = path;
        yield cb.lessThanOrEqualTo(comparablePath, (Comparable) value);
      }
      case LIKE -> cb.like(path.as(String.class), "%" + value + "%");
      case IN -> {
        if (criteria.values() != null && !criteria.values().isEmpty()) {
          List<Object> convertedValues =
              criteria.values().stream().map(v -> convertValue(v, path.getJavaType())).toList();
          yield path.in(convertedValues);
        }
        yield null;
      }
    };
  }

  private static <T> Path<Object> getPath(Root<T> root, String field) {
    // Handle nested properties (e.g., "user.name")
    String[] parts = field.split("\\.");
    Path<Object> path = root.get(parts[0]);

    for (int i = 1; i < parts.length; i++) {
      path = path.get(parts[i]);
    }

    return path;
  }

  private static Object convertValue(Object value, Class<?> targetType) {
    if (value == null) {
      return null;
    }

    String valueStr = value.toString();

    // formatter
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Handle different data types
    if (targetType == String.class) {
      return valueStr;
    } else if (targetType == Integer.class || targetType == int.class) {
      return Integer.valueOf(valueStr);
    } else if (targetType == Long.class || targetType == long.class) {
      return Long.valueOf(valueStr);
    } else if (targetType == Double.class || targetType == double.class) {
      return Double.valueOf(valueStr);
    } else if (targetType == Boolean.class || targetType == boolean.class) {
      return Boolean.valueOf(valueStr);
    } else if (targetType == UUID.class) {
      return UUID.fromString(valueStr);
    } else if (targetType == LocalDate.class) {
      try {
        return LocalDate.parse(valueStr, dateFormatter);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid date format. Expected format: dd-MM-yyyy", e);
      }
    } else if (targetType == LocalDateTime.class) {
      try {
        return LocalDateTime.parse(valueStr, dateTimeFormatter);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException(
            "Invalid datetime format. Expected format: dd-MM-yyyy HH:mm:ss", e);
      }
    } else if (targetType.isEnum()) {
      try {
        if (targetType == SubscriptionDuration.class) {
          return SubscriptionDuration.findByDisplayName(valueStr);
        }

        Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) targetType;
        return Enum.valueOf((Class) enumType, valueStr.toUpperCase());
      } catch (IllegalArgumentException e) {

        Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) targetType;
        throw new InvalidEnumException("Invalid enum constant", enumType);
      }
    }

    // If we can't convert, return the original value
    return value;
  }
}
