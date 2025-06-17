package com.sangtandoan.sub_tracker.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserRequest {

  @Email(message = "must be a valid email")
  @NotBlank(message = "can not be blank")
  private String email;

  @NotBlank(message = "can not be null")
  private String name;

  @NotBlank(message = "can not be blank")
  @Size(min = 6, message = "must be longer than 6 characters")
  private String password;
}
