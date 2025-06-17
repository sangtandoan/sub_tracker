package com.sangtandoan.sub_tracker.user;

import io.jsonwebtoken.lang.Collections;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
  private final UserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var user =
        this.userRepo
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new User(
        user.getEmail(),
        new String(user.getPassword(), StandardCharsets.UTF_8),
        Collections.emptyList());
  }
}
