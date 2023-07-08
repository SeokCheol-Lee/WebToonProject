package com.example.webtoonproject.dto;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.type.Authority;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

public class Auth {
  @Data
  @Builder
  public static class SignIn{
    @NotNull
    private String userId;
    @NotNull
    private String password;
  }

  @Data
  @Builder
  public static class ResponseSignup{
    @NotNull
    private String userId;
    @NotNull
    private Authority role;
  }

  @Data
  @Builder
  public static class SignUp{
    @NotNull
    private String userId;
    @NotNull
    private String userName;
    @NotNull
    private String userPassword;

    public User toEntity(){
      return User.builder()
          .userId(this.userId)
          .userName(this.userName)
          .userPassword(this.userPassword)
          .role(Authority.ROLE_USER)
          .build();
    }
  }

  @Data
  @Builder
  public static class ResponseToken{
    private String token;
  }
}
