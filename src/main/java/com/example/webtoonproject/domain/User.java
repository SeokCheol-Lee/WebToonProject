package com.example.webtoonproject.domain;

import com.example.webtoonproject.type.Authority;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long id;

  private String userName;
  private String userId;
  private String userPassword;
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private LocalDateTime registerDate;
  @Enumerated(EnumType.STRING)
  private Authority role;
  private Long cash;

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
