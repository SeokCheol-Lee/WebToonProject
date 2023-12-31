package com.example.webtoonproject.security;

import com.example.webtoonproject.service.AuthService;
import com.example.webtoonproject.type.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {
  private static final String KEY_ROLE = "role";
  private static final long TOKEN_EXPIRE_TIME = 1000*60*60*24;
  private final AuthService authService;

  @Value("${spring.jwt.secret}")
  private String secretKey;
  public String generateToken(String userId, Authority role){
    Claims claims = Jwts.claims().setSubject(userId);
    claims.put(KEY_ROLE,role);

    var now = new Date();
    var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS256, this.secretKey)
        .compact();
  }
  public Authentication getAuthentication(String jwt){
    UserDetails userDetails = this.authService.loadUserByUsername(this.getUserId(jwt));
    return new UsernamePasswordAuthenticationToken(
        userDetails, "",userDetails.getAuthorities());
  }

  public String getUserId(String token){
    return this.parseClaims(token).getSubject();
  }
  public boolean validateToken(String token){
    if(!StringUtils.hasText(token)) return false;

    var claims = this.parseClaims(token);
    return !claims.getExpiration().before(new Date());
  }

  private Claims parseClaims(String token){
    try{
      return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
    }catch (ExpiredJwtException e){
      return e.getClaims();
    }
  }
}
