package com.example.webtoonproject.repository;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository {
    Optional<Account> findFirstByOrderByIdDesc();

    Optional<Account> findBy(User user);
}
