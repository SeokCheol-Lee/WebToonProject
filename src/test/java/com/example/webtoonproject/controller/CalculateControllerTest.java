package com.example.webtoonproject.controller;

import com.example.webtoonproject.domain.Account;
import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.Calculate;
import com.example.webtoonproject.exception.AccountException;
import com.example.webtoonproject.repository.AccountRepository;
import com.example.webtoonproject.service.AccountService;
import com.example.webtoonproject.type.Authority;
import com.example.webtoonproject.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import static org.assertj.core.api.Assertions.*;


@Slf4j
@Transactional
@SpringBootTest
class CalculateControllerTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    private static final String BASE_URL = "/calculate";

    @Test
    @DisplayName("lock test")
    void addCashLock() throws InterruptedException {
        //여러 스레드에서 동시에 엑세스할 수 있는 원자 정수 카운터
        AtomicInteger successCount = new AtomicInteger();
        int numberOfExcute = 10;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfExcute);

        User user = User.builder()
                .userId("wnstj")
                .role(Authority.ROLE_USER)
                .build();

        accountRepository.save(Account.builder()
                .accountNumber("1000")
                .accountUser(user)
                .build());

        Calculate.AddCash request = Calculate.AddCash.builder().cash(1000L).build();

        for (int i = 0; i < numberOfExcute; i++) {
            service.execute(() -> {
                try {
                   accountService.addCash(request, user);
                   successCount.getAndIncrement();
                   log.info("성공");
                }catch (ObjectOptimisticLockingFailureException oe){
                    log.info("충돌");
                }catch (Exception e){
                    log.error(String.valueOf(e));
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
    void useCash() {
    }
}