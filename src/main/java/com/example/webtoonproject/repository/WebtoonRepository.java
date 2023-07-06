package com.example.webtoonproject.repository;

import com.example.webtoonproject.domain.WebtoonUrl;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonRepository extends JpaRepository<WebtoonUrl,Long> {
  List<WebtoonUrl> findWebtoonByWebtoonNameAndWebtoonChapter(String webtoonName, String webtoonChapter);
  boolean existsByWebtoonName(String webtoonName);
}
