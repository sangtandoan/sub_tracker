package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.user.User;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class SubscriptionSpecifications {

  public static Specification<Subscription> search(String search, Pageable pageable) {
    return (root, query, cb) -> {
      if (search == null || search.isBlank()) return null;

      var ftsPredicate =
          cb.isTrue(
              cb.function(
                  "tsvector_match",
                  Boolean.class,
                  root.get(Subscription_.nameTsv),
                  cb.function(
                      "plainto_tsquery", String.class, cb.literal("english"), cb.literal(search))));

      // Add sorting
      List<Order> orders = new ArrayList<>();

      var isSortByRelevance =
          pageable.getSort().stream().anyMatch(s -> s.getProperty().equals("relevance"));

      if (isSortByRelevance) {
        orders.add(
            cb.desc(
                cb.function(
                    "ts_rank",
                    Double.class,
                    root.get(Subscription_.nameTsv),
                    cb.function(
                        "plainto_tsquery",
                        String.class,
                        cb.literal("english"),
                        cb.literal(search)))));
      }
      ;
      pageable.getSort().stream()
          .filter(s -> !s.getProperty().equals("relevance"))
          .forEach(
              s -> {
                System.out.println(s.getProperty());
                Path<?> path = root.get(s.getProperty());
                orders.add(s.getDirection() == Sort.Direction.ASC ? cb.asc(path) : cb.desc(path));
              });

      query.orderBy(orders);

      return ftsPredicate;
    };
  }

  public static Specification<Subscription> belongsToUser(User user) {
    return (root, query, cb) -> {
      if (user == null) return null;
      return cb.equal(root.get(Subscription_.user), user);
    };
  }

  public static Specification<Subscription> isCancelled(Boolean isCancelled) {
    return (root, query, cb) -> {
      if (isCancelled == null) return null;

      return cb.equal(root.get(Subscription_.isCancelled), isCancelled);
    };
  }

  public static Specification<Subscription> expiringInDays(int days) {
    return (root, query, cb) -> {
      var exactDate = LocalDate.now().plusDays(days);
      var path = root.get(Subscription_.endDate);

      return cb.and(cb.equal(path, exactDate), cb.greaterThan(path, exactDate.minusDays(1)));
    };
  }
}
