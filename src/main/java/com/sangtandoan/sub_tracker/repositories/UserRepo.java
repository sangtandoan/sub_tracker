package com.sangtandoan.sub_tracker.repositories;

import com.sangtandoan.sub_tracker.entities.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);
}
