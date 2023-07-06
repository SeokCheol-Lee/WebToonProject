package com.example.webtoonproject.dto;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.type.Authority;
import lombok.Builder;
import lombok.Data;

public class Auth {
  @Data
  @Builder
  public static class SignIn{
    private String userId;
    private String password;
  }

  @Data
  @Builder
  public static class ResponseSignup{
    private String userId;
    private Authority role;
  }

  @Data
  @Builder
  public static class SignUp{
    private String userId;
    private String userName;
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
}
