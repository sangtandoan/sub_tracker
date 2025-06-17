package com.sangtandoan.sub_tracker.user;

import java.util.UUID;
import lombok.Data;

@Data
public class UserDto {

  private UUID id;
  private String email;
  private String name;
}
