package com.sangtandoan.sub_tracker.token;

import java.util.UUID;

public interface Token {

  boolean isValid();

  String toString();

  UUID getUserId();
}
