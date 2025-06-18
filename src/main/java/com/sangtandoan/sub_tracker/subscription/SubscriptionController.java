package com.sangtandoan.sub_tracker.subscription;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
  private final SubscriptionService subscriptionService;

  @GetMapping
  public ResponseEntity<?> findAll() {

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById() {
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody CreateSubscriptionRequest request) {
    var response = this.subscriptionService.create(request);
    var uri =
        UriComponentsBuilder.fromPath("/api/v1/subscriptions/{id}")
            .buildAndExpand(response.id())
            .toUri();

    return ResponseEntity.created(uri).body(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updatePartial() {
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete() {
    return ResponseEntity.ok().build();
  }
}
