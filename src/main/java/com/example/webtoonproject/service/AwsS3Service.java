package com.example.webtoonproject.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.webtoonproject.domain.User;
import com.example.webtoonproject.domain.Webtoon;
import com.example.webtoonproject.domain.WebtoonUrl;
import com.example.webtoonproject.dto.WebtoonDto;
import com.example.webtoonproject.exception.WebtoonException;
import com.example.webtoonproject.repository.WebtoonRepository;
import com.example.webtoonproject.repository.WebtoonUrlRepository;
import com.example.webtoonproject.type.ErrorCode;
import com.example.webtoonproject.utils.CommonUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  private final WebtoonUrlRepository webtoonUrlRepository;
  private final WebtoonRepository webtoonRepository;

  public String uploadFile(List<MultipartFile> multipartFiles, WebtoonDto.Upload request, User user) {
    if(multipartFiles.isEmpty()){
      throw new WebtoonException(ErrorCode.VALIDATE_FILE_EXISTS);
    }

    List<WebtoonUrl> webtoonUrlList = new ArrayList<>();

    for(MultipartFile multipartFile : multipartFiles){
      String fileName = CommonUtils.buildFileName(
          request.getWebtoonName(), request.getWebtoonChapter(),
          multipartFile.getOriginalFilename());
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(multipartFile.getContentType());

      try(InputStream inputStream = multipartFile.getInputStream()) {
        amazonS3.putObject(new PutObjectRequest(bucket,fileName,inputStream,objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        String url = amazonS3.getUrl(bucket,fileName).toString();

        Webtoon webtoon = existsWebtoon(request.getWebtoonName(), user);

        WebtoonUrl webtoonUrl = WebtoonUrl.builder()
            .webtoon(webtoon)
            .webtoonChapter(request.getWebtoonChapter())
            .webtoonUrl(url)
            .build();
        webtoonUrlList.add(webtoonUrl);
      } catch (IOException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
      }
    }
    this.webtoonUrlRepository.saveAll(webtoonUrlList);
    return webtoonUrlList.size() + " files upload";
  }

  public Webtoon existsWebtoon(String webtoonName, User user){
    if(webtoonRepository.existsByWebtoonName(webtoonName)){
      return webtoonRepository.findWebtoonByWebtoonName(webtoonName)
          .orElseThrow(() -> new WebtoonException(ErrorCode.VALIDATE_WEBTOON_EXISTS));
    }else{
      return webtoonRepository.save(Webtoon.builder()
          .webtoonName(webtoonName)
          .donation(0L)
          .user(user)
          .build());
    }
  }

  public List<String> downloadFile(String webtoonName, String webtoonChapter){
    Webtoon webtoon = webtoonRepository.findWebtoonByWebtoonName(webtoonName)
        .orElseThrow(() -> new WebtoonException(ErrorCode.VALIDATE_WEBTOON_EXISTS));
    List<WebtoonUrl> list = webtoonUrlRepository.findWebtoonUrlByWebtoonAndWebtoonChapter(
        webtoon, webtoonChapter);
    List<String> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      result.add(list.get(i).getWebtoonUrl());
    }
    return result;
  }

  public void deleteFile(String fileName) {
    amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
  }

  private String createFileName(String fileName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
    return UUID.randomUUID().toString().concat(getFileExtension(fileName));
  }

  private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    try {
      return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
    }
  }
}