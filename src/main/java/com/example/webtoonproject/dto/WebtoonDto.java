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

    public Webtoon toEntity(String url){
      return Webtoon.builder()
          .webtoonName(this.getWebtoonName())
          .webtoonChapter(this.getWebtoonChapter())
          .webtoonUrl(url)
          .build();
    }
  }

  @Data
  @Builder
  public static class Download{
    private String webtoonName;
    private String webtoonChapter;

    public Webtoon toEntity(String url){
      return Webtoon.builder()
          .webtoonName(this.getWebtoonName())
          .webtoonChapter(this.getWebtoonChapter())
          .webtoonUrl(url)
          .build();
    }
  }

}
