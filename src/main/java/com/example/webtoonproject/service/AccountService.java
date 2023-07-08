package com.example.webtoonproject.service;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.Calculate;
import com.example.webtoonproject.exception.AuthException;
import com.example.webtoonproject.repository.AccountRepository;
import com.example.webtoonproject.repository.UserRepository;
import com.example.webtoonproject.type.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.Locale;
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
                .accountUser(user)
                .accountNumber(newAccountNumber)
                .balance(0L)
                .build();

        accountRepository.save(account);
        return account;
    }

    @Transactional
    public Account getAccount(User user){
        Account account = accountRepository.findAllByAccountUser(user)
            .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));
        return account;
    }

    @Transactional
    public Account addCash(Calculate.AddCash cash, User user){
        Account account = accountRepository.findAllByAccountUser(user)
            .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));
        account.addBalance(cash.getCash());
        return account;
    }

    @Transactional
    public Account useCash(Calculate.UseCash cash, User user){
        Account account = accountRepository.findAllByAccountUser(user)
            .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));
        account.useBalance(cash.getCash());
        return account;
    }

}
