package com.example.webtoonproject.dto;

import com.example.webtoonproject.domain.Webtoon;
import lombok.Builder;
import lombok.Data;

public class WebtoonDto {

  @Data
  @Builder
  public static class Upload{
    private String webtoonName;
    private String webtoonChapter;
  }

  @Data
  @Builder
  public static class Download{
    private String webtoonName;
    private String webtoonChapter;
  }

}
