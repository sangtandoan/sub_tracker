package com.sangtandoan.sub_tracker.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleUserInfo {
  @JsonProperty("sub")
  private String id;

  private String email;
  private String name;

  private String givenName;

  private String familyName;

  private String picture;
}
