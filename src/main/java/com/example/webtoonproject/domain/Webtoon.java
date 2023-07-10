package com.example.webtoonproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Webtoon {
  @Id
  @GeneratedValue
  @Column(name = "WEBTOON_ID")
  private Long id;

  @Column(name = "WEBTOON_NAME")
  private String webtoonName;
  private Long donation;

  @ManyToOne
  @JoinColumn(name = "USERTABLE_ID")
  private User user;

  @CreatedDate
  private LocalDateTime registerDate;

  public void addDonation(Long amount){
    donation += amount;
  }
  public Long getDonation(){
    Long result = donation;
    donation = 0L;
    return result;
  }
}
