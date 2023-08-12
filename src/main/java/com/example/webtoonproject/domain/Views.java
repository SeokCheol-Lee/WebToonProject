package com.example.webtoonproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Views {
  @Id
  @GeneratedValue
  @Column(name = "WEBTOON_ID")
  private Long id;
  @OneToOne
  private Webtoon webtoon_id;
  private Long view;
}
