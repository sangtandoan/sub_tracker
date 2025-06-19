package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subscriptions")
public class Subscription {

  @Id @GeneratedValue private UUID id;

  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private boolean isCancelled = false;

  private SubscriptionDuration duration;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public void setEndDate(LocalDate startDate, SubscriptionDuration duration) {
    this.endDate = duration.getEndDate(startDate);
  }

  public void setEndDate() {
    this.endDate = this.duration.getEndDate(this.startDate);
  }
}
