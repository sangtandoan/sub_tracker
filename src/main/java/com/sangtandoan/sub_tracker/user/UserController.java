package com.sangtandoan.sub_tracker.user;

import com.sangtandoan.sub_tracker.user.exceptions.UserExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<?> createNewUser(@Valid @RequestBody NewUserRequest request) {
    var user = this.userRepo.findByEmail(request.getEmail()).orElse(null);
    if (user != null) {
      throw new UserExistsException();
    }

    user = this.userMapper.toEntity(request);
    var hashedPassword = this.passwordEncoder.encode(request.getPassword());
    user.setPassword(hashedPassword.getBytes());

    this.userRepo.save(user);

    var userUrl =
        UriComponentsBuilder.fromUriString("/users/{id}").buildAndExpand(user.getId()).toUri();

    return ResponseEntity.created(userUrl).body(this.userMapper.toDto(user));
  }

  @GetMapping
  public ResponseEntity<?> getAllUsers() {
    var users = this.userRepo.findAll();

    return ResponseEntity.ok(users);
  }
}
