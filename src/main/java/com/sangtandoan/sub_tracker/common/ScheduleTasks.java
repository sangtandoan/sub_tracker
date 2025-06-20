package com.sangtandoan.sub_tracker.common;

import com.sangtandoan.sub_tracker.email.EmailService;
import com.sangtandoan.sub_tracker.email.ExpiringNoti;
import com.sangtandoan.sub_tracker.subscription.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class ScheduleTasks {
  private static final int[] DAYS = {7, 5, 3, 1};

  private final SubscriptionService subscriptionService;
  private final EmailService emailService;

  @Scheduled(cron = "0 12 18 * * ?")
  public void checkExpiringSubscriptions() {
    log.info("Checking for expiring subscriptions...");

    for (int days : DAYS) {
      Thread.startVirtualThread(
          () -> {
            var expiring = this.subscriptionService.findExpiringInDays(days);
            expiring.forEach(
                sub -> {
                  var noti =
                      new ExpiringNoti(
                          sub.getUser().getName(), sub.getName(), days, sub.getUser().getEmail());
                  this.emailService.sendExpiringNoti(noti);
                });
          });
    }
  }
}
