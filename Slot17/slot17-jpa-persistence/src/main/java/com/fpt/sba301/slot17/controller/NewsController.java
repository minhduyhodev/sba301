package com.fpt.sba301.slot17.controller;

import com.fpt.sba301.slot17.dto.NewsCreateRequest;
import com.fpt.sba301.slot17.dto.NewsDetailResponse;
import com.fpt.sba301.slot17.dto.NewsSummaryResponse;
import com.fpt.sba301.slot17.dto.NewsUpdateRequest;
import com.fpt.sba301.slot17.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<Page<NewsSummaryResponse>> getLatestNews(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(newsService.getLatestNews(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDetailResponse> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<NewsSummaryResponse>> searchNews(@RequestParam String keyword) {
        return ResponseEntity.ok(newsService.searchByTitle(keyword));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<NewsSummaryResponse>> getNewsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(newsService.getNewsByCategory(categoryId));
    }

    @PostMapping
    public ResponseEntity<NewsDetailResponse> createNews(@Valid @RequestBody NewsCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(newsService.createNews(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDetailResponse> updateNews(
            @PathVariable Long id,
            @Valid @RequestBody NewsUpdateRequest request) {
        return ResponseEntity.ok(newsService.updateNews(id, request));
    }

    @PatchMapping("/{id}/move-category")
    public ResponseEntity<Void> moveCategory(
            @PathVariable Long id,
            @RequestParam Long targetCategoryId) {
        newsService.moveCategory(id, targetCategoryId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}
