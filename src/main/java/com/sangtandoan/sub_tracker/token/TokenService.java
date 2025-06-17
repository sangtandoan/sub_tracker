package com.sangtandoan.sub_tracker.token;

import com.sangtandoan.sub_tracker.user.User;
import java.util.Optional;

public interface TokenService {

  Token generate(User user);

  Optional<Token> parse(String token);
}
