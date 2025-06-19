package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.exceptions.BaseAppException;
import org.springframework.http.HttpStatus;

public class SubscriptionNotFoundException extends BaseAppException {

  public SubscriptionNotFoundException() {
    super("Subcripton not found");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
