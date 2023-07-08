package com.example.webtoonproject.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

public class WebtoonDto {

  @Data
  @Builder
  public static class Upload{
    @NotNull
    private String webtoonName;
    @NotNull
    private String webtoonChapter;
  }

  @Data
  @Builder
  public static class Download{
    @NotNull
    private String webtoonName;
    @NotNull
    private String webtoonChapter;
  }

}
