package com.example.webtoonproject.service;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.Auth;
import com.example.webtoonproject.exception.AuthException;
import com.example.webtoonproject.repository.UserRepository;
import com.example.webtoonproject.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository.findUserByUserId(username)
        .orElseThrow(() -> new UsernameNotFoundException("couldn't find user ->" + username));
  }

  public User register(Auth.SignUp user){
    boolean exists = this.userRepository.existsByUserId(user.getUserId());
    if(exists){
      throw new AuthException(ErrorCode.EXIST_USERID);
    }
    user.setUserPassword(this.passwordEncoder.encode(user.getUserPassword()));
    User result = this.userRepository.save(user.toEntity());
    return result;
  }

  public User authenticate(Auth.SignIn user){
    User req = this.userRepository.findUserByUserId(user.getUserId())
        .orElseThrow(() -> new AuthException(ErrorCode.NOT_EXIST_USERID));
    if(!this.passwordEncoder.matches(user.getPassword(),req.getPassword())){
      throw new AuthException(ErrorCode.NOT_MATCH_USERPASSWORD);
    }
    return req;
  }
}
