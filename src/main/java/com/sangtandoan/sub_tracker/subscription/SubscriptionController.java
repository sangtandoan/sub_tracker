package com.sangtandoan.sub_tracker.subscription;

import com.sangtandoan.sub_tracker.common.filter.FilterParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
  private final SubscriptionService subscriptionService;

  @GetMapping
  public ResponseEntity<?> findAll(
      @PageableDefault(size = 10, page = 0, sort = "endDate", direction = Direction.ASC)
          Pageable pageable,
      @RequestParam(required = false) Boolean isCancelled,
      HttpServletRequest request) {

    var response = this.subscriptionService.findAllWithFilters(pageable, isCancelled, request.getParameterMap());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  public ResponseEntity<?> search(
      @RequestParam(required = true) String searchTerm,
      @RequestParam Boolean isCancelled,
      @PageableDefault(
              page = 0,
              size = 10,
              sort = {"relevance", "endDate"},
              direction = Direction.ASC)
          Pageable pageable) {

    var response = this.subscriptionService.search(searchTerm, pageable, isCancelled);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable UUID id) {
    var response = this.subscriptionService.findById(id);

    return ResponseEntity.ok(response);
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
  public ResponseEntity<?> updatePartial(
      @PathVariable UUID id, @RequestBody UpdateSubscriptionRequest request) {

    var response = this.subscriptionService.partialUpdate(id, request);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable UUID id) {
    this.subscriptionService.delete(id);

    return ResponseEntity.ok("Delete successfully.");
  }
}
