package com.example.webtoonproject.repository;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.User;
import jakarta.persistence.LockModeType;
import lombok.Lombok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByOrderByIdDesc();

    @Lock(LockModeType.READ)
    Optional<Account> findAllByAccountUser(User user);

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String AccountNumber);

}
