package com.example.webtoonproject.exception;

import com.example.webtoonproject.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebtoonException extends RuntimeException{
  private ErrorCode errorCode;
  private String errorMessage;

  public WebtoonException(ErrorCode errorCode){
    this.errorCode = errorCode;
    this.errorMessage = errorCode.getDescription();
  }
}
