package com.fpt.sba301.slot17.repository;

import com.fpt.sba301.slot17.domain.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByCategoryId(Long categoryId);

    List<News> findByTitleContainingIgnoreCase(String keyword);

    Page<News> findByActiveTrueOrderByPublishedAtDesc(Pageable pageable);

    List<News> findTop10ByActiveTrueOrderByPublishedAtDesc();

    boolean existsByTitleIgnoreCase(String title);
}
