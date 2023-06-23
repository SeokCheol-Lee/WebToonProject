package com.example.webtoonproject.repository;

import com.example.webtoonproject.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findUserByUserId(String userId);
  boolean existsByUserId(String userId);
}
