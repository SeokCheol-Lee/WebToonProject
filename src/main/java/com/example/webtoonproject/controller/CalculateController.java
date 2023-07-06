package com.example.webtoonproject.controller;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.Calculate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/calculate")
public class CalculateController {

    @PostMapping("/addcash")
    public ResponseEntity<?> addCash(@RequestBody Calculate.AddCash cash,
                                     @AuthenticationPrincipal User user){
        return null;
    }
}
