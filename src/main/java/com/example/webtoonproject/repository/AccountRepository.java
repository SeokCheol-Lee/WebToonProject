package com.example.webtoonproject.repository;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.User;
import lombok.Lombok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByOrderByIdDesc();

    Optional<Account> findAllByAccountUser(User user);
}