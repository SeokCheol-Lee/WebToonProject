package com.example.webtoonproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.Auth;
import com.example.webtoonproject.dto.Auth.SignIn;
import com.example.webtoonproject.exception.AuthException;
import com.example.webtoonproject.repository.UserRepository;
import com.example.webtoonproject.type.Authority;
import com.example.webtoonproject.type.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Transactional
@SpringBootTest
class AuthServiceTest {

  @Autowired
  AuthService authService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  @AfterEach
  public void cleanup(){userRepository.deleteAll();}

  @Test
  @DisplayName("회원가입 성공")
  void successRegister() {
    //given
    Auth.SignUp user = Auth.SignUp.builder()
        .userId("wnstj")
        .userName("test")
        .userPassword("qwe123").build();

    //when
    authService.register(user);

    //then
    Optional<User> u1 = userRepository.findUserByUserId("wnstj");

    assertEquals(user.getUserName(),u1.get().getUsername());
  }
  @Test
  @DisplayName("회원가입 실패 - 동일한 Id 존재")
  void failRegister() {
    //given
    Auth.SignUp user = Auth.SignUp.builder()
        .userId("wnstj")
        .userName("test")
        .userPassword("qwe123").build();

    authService.register(user);

    //when
    AuthException authException = assertThrows(AuthException.class,
        () -> authService.register(user));

    //then
    assertEquals(ErrorCode.EXIST_USERID, authException.getErrorCode());
  }

  @Test
  @DisplayName("인증성공(로그인)")
  void successAuthenticate() {
    //given
    User user = User.builder()
        .userName("wnstj")
        .userId("test")
        .userPassword(passwordEncoder.encode("qwe123"))
        .role(Authority.ROLE_USER)
        .build();
    userRepository.save(user);

    SignIn si = SignIn.builder()
        .userId("test")
        .password("qwe123")
        .build();

    //when
    User ans = authService.authenticate(si);

    //then
    assertEquals(ans,user);
  }
  @Test
  @DisplayName("인증실패(로그인) - pw매칭 실패")
  void failAuthenticate() {
    //given
    User user = User.builder()
        .userName("wnstj")
        .userId("test")
        .userPassword("qwe123")
        .role(Authority.ROLE_USER)
            .build();
    userRepository.save(user);

    SignIn si = SignIn.builder()
        .userId("test")
        .password("qwe123")
        .build();

    //when
    AuthException authException = assertThrows(AuthException.class,
        () -> authService.authenticate(si));

    //then
    assertEquals(ErrorCode.NOT_MATCH_USERPASSWORD,authException.getErrorCode());
  }
}