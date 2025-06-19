package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.user.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscriptionRepo
    extends JpaRepository<Subscription, UUID>, JpaSpecificationExecutor<Subscription> {
  Page<Subscription> findAllByUser(User user, Pageable pageable);

  @Query(
      value =
          "SELECT s.* FROM subscriptions s "
              + "WHERE s.user_id = :userId "
              + "AND s.name_tsv @@ plainto_tsquery('english', :searchTerm) ",
      countQuery =
          "SELECT COUNT(*) FROM subscriptions s "
              + "WHERE s.user_id = :userId "
              + "AND s.name_tsv @@ plainto_tsquery('english', :searchTerm)",
      nativeQuery = true)
  Page<Subscription> searchByName(
      @Param("userId") UUID userId, @Param("searchTerm") String searchTerm, Pageable pageable);
}
