package com.example.webtoonproject.controller;

import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.dto.WebtoonDto;
import com.example.webtoonproject.service.AwsS3Service;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/webtoon")
public class WebtoonController {

  private final AwsS3Service awsS3Service;

  @PostMapping("/upload")
  public ResponseEntity<?> uploadFile(@RequestPart List<MultipartFile> images,
      @RequestPart(value = "webtoon") WebtoonDto.Upload request, @AuthenticationPrincipal User user){
    return ResponseEntity.ok(awsS3Service.uploadFile(images,request,user));
  }

  @GetMapping("/download")
  public ResponseEntity<?> downloadFile(
      @RequestParam String webtoonName, String webtoonChapter){
    return ResponseEntity.ok(awsS3Service.downloadFile(webtoonName,webtoonChapter));
  }

}
