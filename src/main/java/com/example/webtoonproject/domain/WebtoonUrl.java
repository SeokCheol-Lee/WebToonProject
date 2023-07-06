package com.example.webtoonproject.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class WebtoonUrl {

  @Id
  @GeneratedValue(strategy =GenerationType.IDENTITY)
  @Column(name = "WEBTOON_URL_ID")
  private Long id;

  private String webtoonName;
  private String webtoonChapter;
  private String webtoonUrl;

  @CreatedDate
  private LocalDateTime registerDate;
}
