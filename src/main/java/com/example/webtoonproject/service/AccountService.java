package com.example.webtoonproject.service;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.exception.AuthException;
import com.example.webtoonproject.repository.AccountRepository;
import com.example.webtoonproject.repository.UserRepository;
import com.example.webtoonproject.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public Account createAccount(String userId){

        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));

        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> (Integer.parseInt(account.getAccountNumber()) + 1 + ""))
                .orElse("1000");

        Account account = Account.builder()
                .accountNumber(userId)
                .accountNumber(newAccountNumber)
                .balance(0L)
                .build();

        accountRepository.save(account);
        return account;
    }

    @Transactional
    public Optional<Account> getAccount(String id){
        User user = userRepository.findUserByUserId(id)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));
        return accountRepository.findBy(user);
    }

}
