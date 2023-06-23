package com.example.webtoonproject.controller;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.Auth;
import com.example.webtoonproject.security.TokenProvider;
import com.example.webtoonproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final TokenProvider tokenProvider;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody Auth.SignUp request){
    User result = this.authService.register(request);
    return ResponseEntity.ok(result);
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody Auth.SignIn request){
    User user = this.authService.authenticate(request);
    String token = this.tokenProvider.generateToken(user.getUserId(),user.getRole());
    return ResponseEntity.ok(token);
  }
}
