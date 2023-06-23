package com.example.webtoonproject.dto;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.type.Authority;
import lombok.Data;

public class Auth {
  @Data
  public static class SignIn{
    private String userId;
    private String password;
  }

  @Data
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
          .cash(0L)
          .build();
    }
  }
}
