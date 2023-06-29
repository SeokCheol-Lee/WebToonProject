package com.example.webtoonproject.controller;

import com.example.webtoonproject.aws.S3Uploader;
import com.example.webtoonproject.service.AwsS3Service;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ResponseEntity<List<String>> uploadFile(@RequestPart List<MultipartFile> images){
    return ResponseEntity.ok(awsS3Service.uploadFile(images));
  }

}
