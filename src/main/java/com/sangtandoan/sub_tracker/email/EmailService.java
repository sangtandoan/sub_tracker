package com.sangtandoan.sub_tracker.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Value("${spring.mail.verify.host}")
  private String verifyHost;

  public void sendExpiringNoti(ExpiringNoti noti) {
    Thread.startVirtualThread(
        () -> {
          SimpleMailMessage message = new SimpleMailMessage();
          message.setFrom(this.fromEmail);
          message.setTo(noti.to());
          message.setText(this.getExpiringMessage(noti));
          message.setSubject("Subscription's Expiration is coming.");

          this.mailSender.send(message);
        });
  }

  private String getExpiringMessage(ExpiringNoti noti) {
    var builder = new StringBuilder();
    builder.append("Hello " + noti.userName() + ", ");
    builder.append("your " + noti.subscriptionName() + " subscription ");
    builder.append("is about to end in " + noti.expiringInDays() + " days.\n\n");
    builder.append("Please cancel it if you dont want to use it anymore.\n\n");
    builder.append("Sincerely, \n");
    builder.append("Subdub.");

    return builder.toString();
  }
}
