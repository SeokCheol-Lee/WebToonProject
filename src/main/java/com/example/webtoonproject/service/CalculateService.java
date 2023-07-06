package com.example.webtoonproject.service;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.exception.AuthException;
import com.example.webtoonproject.repository.UserRepository;
import com.example.webtoonproject.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculateService {

    private final UserRepository userRepository;

    @Transactional
    public Long addCash(String userId, Long cash){
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));


        return null;
    }
}
