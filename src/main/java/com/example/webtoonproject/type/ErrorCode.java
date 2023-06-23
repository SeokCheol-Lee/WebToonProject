package com.example.webtoonproject.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  AMOUNT_EXCEED_CASH("거래 금액이 잔액보다 큽니다."),
  EXIST_USERID("이미 사용 중인 아이디입니다."),
  NOT_EXIST_USERID("존재하지 않는 아이디입니다."),
  NOT_MATCH_USERPASSWORD("일치하지 않는 비밀번호입니다.");

  private final String description;
}
