package com.sangtandoan.sub_tracker.dtos;

import lombok.Data;

@Data
public class NewUserRequest {
  private String email;
  private String name;
  private String password;
}
