package com.example.webtoonproject.repository;

import com.example.webtoonproject.domain.Webtoon;
import com.example.webtoonproject.domain.WebtoonUrl;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonUrlRepository extends JpaRepository<WebtoonUrl,Long> {
  List<WebtoonUrl> findWebtoonUrlByWebtoonAndWebtoonChapter(Webtoon webtoonName, String webtoonChapter);
}
