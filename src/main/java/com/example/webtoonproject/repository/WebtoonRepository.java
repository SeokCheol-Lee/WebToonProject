package com.example.webtoonproject.repository;

import com.example.webtoonproject.domain.Webtoon;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
  boolean existsByWebtoonName(String webtoonName);
  Optional<Webtoon> findWebtoonByWebtoonName(String webtoonName);
}
