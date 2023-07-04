package com.example.webtoonproject.utils;

import java.nio.charset.StandardCharsets;
import org.springframework.http.ContentDisposition;

public class CommonUtils {
  private static final String FILE_EXTENSION_SEPARATOR = ".";
  private static final String CATEGORY_PREFIX = "/";
  private static final String TIME_SEPARATOR = "_";
  private static final int UNDER_BAR_INDEX = 1;

  public static String buildFileName(String webtoonName, String webtoonChapter, String originalFileName) {
    int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
    String fileExtension = originalFileName.substring(fileExtensionIndex);
    String fileName = originalFileName.substring(0, fileExtensionIndex);
    String now = String.valueOf(System.currentTimeMillis());

    return webtoonName + CATEGORY_PREFIX + webtoonChapter + CATEGORY_PREFIX +
        fileName + TIME_SEPARATOR + now + fileExtension;
  }
  public static ContentDisposition createContentDisposition(String webtoonNameWithFileName) {
    String fileName = webtoonNameWithFileName.substring(
        webtoonNameWithFileName.lastIndexOf(CATEGORY_PREFIX) + UNDER_BAR_INDEX);
    return ContentDisposition.builder("attachment")
        .filename(fileName, StandardCharsets.UTF_8)
        .build();
  }
}
