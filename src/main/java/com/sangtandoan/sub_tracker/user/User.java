package com.sangtandoan.sub_tracker.user;

import com.sangtandoan.sub_tracker.subscription.Subscription;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {

  @EqualsAndHashCode.Include @Id @GeneratedValue private UUID id;

  private String email;
  private String name;
  private byte[] password;
  private boolean emailVerified = false;

  @Column(insertable = false, updatable = false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt = LocalDateTime.now();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserProvider> providers = new HashSet<>();

  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.MERGE, CascadeType.REMOVE},
      orphanRemoval = true)
  private Set<Subscription> subscriptions = new HashSet<>();
}
