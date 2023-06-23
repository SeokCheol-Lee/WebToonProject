package com.example.webtoonproject.exception;

import com.example.webtoonproject.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class AuthException extends RuntimeException{
  private ErrorCode errorCode;
  private String errorMessage;

  public AuthException(ErrorCode errorCode){
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getDescription();

    log.error(errorMessage);
  }

}
