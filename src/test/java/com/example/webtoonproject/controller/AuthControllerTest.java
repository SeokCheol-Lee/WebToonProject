package com.example.webtoonproject.controller;


import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.Auth;
import com.example.webtoonproject.dto.Auth.SignIn;
import com.example.webtoonproject.dto.Auth.SignUp;
import com.example.webtoonproject.repository.UserRepository;
import com.example.webtoonproject.security.TokenProvider;
import com.example.webtoonproject.service.AuthService;
import com.example.webtoonproject.type.Authority;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockBean
  private AuthService authService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private TokenProvider tokenProvider;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserRepository userRepository;

  private static final String BASE_URL = "/auth";

  @Test
  @DisplayName("회원가입 성공")
  void successSignup() throws Exception {
    //given
    Auth.SignUp request = SignUp.builder()
        .userId("wnstj")
        .userName("test")
        .userPassword("qwe123")
        .build();

    given(authService.register(request))
        .willReturn(User.builder()
            .id(1L)
            .userName("test")
            .userId("wnstj")
            .userPassword(passwordEncoder.encode("qwe123"))
            .registerDate(LocalDateTime.now())
            .role(Authority.ROLE_USER)
            .build());

    //when
    String body = objectMapper.writeValueAsString(request);

    //then
    mockMvc.perform(post(BASE_URL + "/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value("wnstj"))
        .andExpect(jsonPath("$.role").value(Authority.ROLE_USER.toString()))
        .andDo(print());
  }

  @Test
  @DisplayName("로그인 성공")
  void successSignin() throws Exception {
    SignIn request = SignIn.builder()
        .userId("wnstj")
        .password("qwe123")
        .build();

    given(authService.authenticate(request))
        .willReturn(User.builder()
            .id(1L)
            .userName("test")
            .userId("wnstj")
            .userPassword(passwordEncoder.encode("qwe123"))
            .registerDate(LocalDateTime.now())
            .role(Authority.ROLE_USER)
            .build());

    String token = tokenProvider.generateToken(request.getUserId(),Authority.ROLE_USER);
    String body = objectMapper.writeValueAsString(request);

    mockMvc.perform(post(BASE_URL + "/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
    )
        .andExpect(status().isOk())
        .andExpect(content().string(token))
        .andDo(print());
  }
}