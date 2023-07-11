package com.example.webtoonproject.controller;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.Calculate;
import com.example.webtoonproject.dto.Calculate.AddCash;
import com.example.webtoonproject.dto.Calculate.UseCash;
import com.example.webtoonproject.exception.AccountException;
import com.example.webtoonproject.service.AccountService;
import com.example.webtoonproject.type.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/calculate")
@RequiredArgsConstructor
public class CalculateController {

    private final AccountService accountService;

    @PostMapping("/addcash")
    public Calculate.ResponseCash addCash(@RequestBody AddCash cash,
                                     @AuthenticationPrincipal User user) throws InterruptedException{
        try {
            Account account = accountService.addCash(cash, user);
            return Calculate.ResponseCash.builder()
                    .cash(account.getBalance())
                    .build();
        }catch (AccountException e){
            accountService.saveFailedUseTransaction(
                    user,
                    cash.getCash(),
                    TransactionType.ADD
            );
            throw e;
        }
    }

    @PostMapping("/usecash")
    public Calculate.ResponseCash useCash(@RequestBody UseCash cash,
        @AuthenticationPrincipal User user) throws InterruptedException{
        try {
            Account account = accountService.useCash(cash, user);
            return Calculate.ResponseCash.builder()
                    .cash(account.getBalance())
                    .build();
        }catch (AccountException e){
            accountService.saveFailedUseTransaction(
                    user,
                    cash.getCash(),
                    TransactionType.USE
            );
            throw e;
        }
    }

    @GetMapping("/mycash")
    public Calculate.ResponseCash myCash(@AuthenticationPrincipal User user){
        Account account = accountService.getAccount(user);
        return Calculate.ResponseCash.builder()
            .cash(account.getBalance())
            .build();
    }
}
