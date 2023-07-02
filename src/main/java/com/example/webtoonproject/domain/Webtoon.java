package com.example.webtoonproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Webtoon {

  @Id
  @GeneratedValue
  private Long id;

  private String webtoonName;
  private String webtoonChapter;
  private String webtoonUrl;
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private LocalDateTime registerDate;
}
