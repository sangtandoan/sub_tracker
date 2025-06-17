package com.sangtandoan.sub_tracker.user;

import com.sangtandoan.sub_tracker.user.exceptions.UserExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @PostMapping
  public ResponseEntity<?> createNewUser(@Valid @RequestBody NewUserRequest requset) {
    var user = this.userRepo.findByEmail(requset.getEmail()).orElse(null);
    if (user != null) {
      throw new UserExistsException();
    }

    user = this.userMapper.toEntity(requset);
    var hashedPassword = this.passwordEncoder.encode(requset.getPassword());
    user.setPassword(hashedPassword.getBytes());

    this.userRepo.save(user);

    var userUrl =
        UriComponentsBuilder.fromUriString("/users/{id}").buildAndExpand(user.getId()).toUri();

    return ResponseEntity.created(userUrl).body(this.userMapper.toDto(user));
  }
}
