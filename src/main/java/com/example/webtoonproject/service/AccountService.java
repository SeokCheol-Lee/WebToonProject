package com.example.webtoonproject.service;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.Transaction;
import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.domain.Webtoon;
import com.example.webtoonproject.dto.Calculate;
import com.example.webtoonproject.exception.AccountException;
import com.example.webtoonproject.exception.AuthException;
import com.example.webtoonproject.exception.WebtoonException;
import com.example.webtoonproject.repository.AccountRepository;
import com.example.webtoonproject.repository.TransactionRepository;
import com.example.webtoonproject.repository.UserRepository;
import com.example.webtoonproject.repository.WebtoonRepository;
import com.example.webtoonproject.type.ErrorCode;
import com.example.webtoonproject.type.TransactionResultType;
import com.example.webtoonproject.type.TransactionType;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.example.webtoonproject.type.TransactionType.ADD;
import static com.example.webtoonproject.type.TransactionType.USE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final TransactionRepository transactionRepository;

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
        try{
            Account account = accountRepository.findAllByAccountUser(user)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));
            saveAndGetTransaction(ADD,TransactionResultType.SUCCESS,cash.getCash(),account);
            account.addBalance(cash.getCash());
            return account;
        }catch (AccountException e){
            saveFailedUseTransaction(user,cash.getCash(), ADD);
            throw e;
        }
    }

    @Transactional
    public Account useCash(Calculate.UseCash cash, User user){
        try{
            Account account = accountRepository.findAllByAccountUser(user)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));
            Webtoon webtoon = webtoonRepository.findWebtoonByWebtoonName(cash.getWebtoonName())
                .orElseThrow(() -> new WebtoonException(ErrorCode.VALIDATE_WEBTOON_EXISTS));
            webtoon.addDonation(cash.getCash());
            saveAndGetTransaction(USE,TransactionResultType.SUCCESS,cash.getCash(),account);
            account.useBalance(cash.getCash());
            return account;
        }catch (AccountException e){
            saveFailedUseTransaction(user, cash.getCash(), USE);
            throw e;
        }
    }

    private Transaction saveAndGetTransaction(
            TransactionType transactionType,
            TransactionResultType transactionResultType,
            Long amount,
            Account account) {
        return transactionRepository.save(
                Transaction.builder()
                        .trasactionType(transactionType)
                        .transactionResultType(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balanceSnapshot(account.getBalance())
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
    }

    @Transactional
    public void saveFailedUseTransaction(User user, Long amount, TransactionType type) {
        Account account = accountRepository.findAllByAccountUser(user)
                .orElseThrow(()-> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
        saveAndGetTransaction(type,TransactionResultType.FAIL, amount, account);
    }
}
