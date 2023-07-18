package com.example.webtoonproject.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.domain.Webtoon;
import com.example.webtoonproject.dto.Auth;
import com.example.webtoonproject.dto.Calculate;
import com.example.webtoonproject.exception.AccountException;
import com.example.webtoonproject.exception.WebtoonException;
import com.example.webtoonproject.repository.AccountRepository;
import com.example.webtoonproject.repository.UserRepository;
import com.example.webtoonproject.repository.WebtoonRepository;
import com.example.webtoonproject.service.AccountService;
import com.example.webtoonproject.service.AuthService;
import com.example.webtoonproject.type.Authority;
import com.example.webtoonproject.type.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class CalculateControllerTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WebtoonRepository webtoonRepository;
    @Autowired
    private UserRepository userRepository;

    private static final String BASE_URL = "/calculate";

    @Test
    @DisplayName("addlock test")
    void addCashLock() throws InterruptedException {
        Auth.SignUp signUp = Auth.SignUp.builder()
            .userId("test")
            .userPassword("test")
            .userName("test")
            .build();

        User user = authService.register(signUp);

        //여러 스레드에서 동시에 엑세스할 수 있는 원자 정수 카운터
        AtomicInteger successCount = new AtomicInteger();
        int numberOfExcute = 10;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfExcute);

        for (int i = 0; i < numberOfExcute; i++) {
            service.execute(() -> {
                try {
                    Calculate.AddCash request = Calculate.AddCash.builder().cash(1000L).build();
                    accountService.addCash(request,user);
                    successCount.getAndIncrement();
                   log.info("성공");
                }catch (Throwable th){
                    log.info("충돌 : " + th);
                }
                latch.countDown();
            });
        }
        latch.await();
        Account result = accountRepository.findByAccountNumber("1000")
                        .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
        assertThat(result.getBalance()).isEqualTo(10000);
    }

    @Test
    @DisplayName("uselock test")
    void useCash() throws InterruptedException {
        User user = User.builder()
            .userName("test")
            .userId("test")
            .userPassword("test")
            .role(Authority.ROLE_USER)
            .build();

        userRepository.save(user);

        Account account = Account.builder()
            .accountUser(user)
            .accountNumber("1000")
            .balance(10000L)
            .build();

        accountRepository.save(account);

        Webtoon webtoon = Webtoon.builder()
            .webtoonName("test")
            .donation(0L)
            .user(user)
            .build();

        webtoonRepository.save(webtoon);

        //여러 스레드에서 동시에 엑세스할 수 있는 원자 정수 카운터
        AtomicInteger successCount = new AtomicInteger();
        int numberOfExcute = 10;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfExcute);

        for (int i = 0; i < numberOfExcute; i++) {
            service.execute(() -> {
                try {
                    Calculate.UseCash request = Calculate.UseCash.builder()
                        .cash(1000L)
                        .webtoonName("test")
                        .build();
                    accountService.useCash(request,user);
                    successCount.getAndIncrement();
                    log.info("성공");
                }catch (Throwable th){
                    log.info("충돌 : " + th);
                }
                latch.countDown();
            });
        }
        latch.await();
        Account result = accountRepository.findByAccountNumber("1000")
            .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
        Webtoon result2 = webtoonRepository.findWebtoonByWebtoonName("test")
                .orElseThrow(() -> new WebtoonException(ErrorCode.VALIDATE_WEBTOON_EXISTS));
        assertThat(result.getBalance()).isEqualTo(0);
        assertThat(result2.getDonation()).isEqualTo(10000L);
    }
}