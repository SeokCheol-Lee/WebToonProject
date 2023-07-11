package com.example.webtoonproject.exception;

import com.example.webtoonproject.type.ErrorCode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class AccountException extends RuntimeException{
  private ErrorCode errorCode;
  private String errorMessage;

  public AccountException(ErrorCode errorCode){
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getDescription();

    log.error(errorMessage);
  }

}
