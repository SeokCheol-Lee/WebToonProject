package com.example.webtoonproject.domain;

import com.example.webtoonproject.type.Authority;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy =GenerationType.IDENTITY)
  @Column(name="USERTABLE_ID")
  private Long id;

  private String userName;
  private String userId;
  private String userPassword;
  @CreatedDate
  private LocalDateTime registerDate;
  @LastModifiedBy
  private LocalDateTime lastUpdateDate;
  @Enumerated(EnumType.STRING)
  private Authority role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    SimpleGrantedAuthority memberAuthority = new SimpleGrantedAuthority(role.toString());
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(memberAuthority);
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.userPassword;
  }

  @Override
  public String getUsername() {
    return this.userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
