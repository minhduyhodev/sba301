package com.fpt.sba301.slot17.service;

import com.fpt.sba301.slot17.domain.Category;
import com.fpt.sba301.slot17.domain.News;
import com.fpt.sba301.slot17.domain.Tag;
import com.fpt.sba301.slot17.dto.NewsCreateRequest;
import com.fpt.sba301.slot17.dto.NewsDetailResponse;
import com.fpt.sba301.slot17.dto.NewsSummaryResponse;
import com.fpt.sba301.slot17.dto.NewsUpdateRequest;
import com.fpt.sba301.slot17.dto.TagResponse;
import com.fpt.sba301.slot17.exception.ResourceNotFoundException;
import com.fpt.sba301.slot17.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    public NewsService(NewsRepository newsRepository, CategoryService categoryService, TagService tagService) {
        this.newsRepository = newsRepository;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @Transactional
    public NewsDetailResponse createNews(NewsCreateRequest request) {
        Category category = categoryService.getRequired(request.getCategoryId());

        News news = new News(request.getTitle().trim(), request.getContent().trim(),
                request.getAuthor().trim(), category);

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            for (String tagName : request.getTagNames()) {
                if (tagName != null && !tagName.isBlank()) {
                    Tag tag = tagService.getOrCreateTag(tagName);
                    news.addTag(tag);
                }
            }
        }

        News saved = newsRepository.save(news);
        return toDetailResponse(saved);
    }

    @Transactional(readOnly = true)
    public NewsDetailResponse getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News article not found with id: " + id));
        return toDetailResponse(news);
    }

    @Transactional(readOnly = true)
    public Page<NewsSummaryResponse> getLatestNews(Pageable pageable) {
        return newsRepository.findByActiveTrueOrderByPublishedAtDesc(pageable)
                .map(this::toSummaryResponse);
    }

    @Transactional(readOnly = true)
    public List<NewsSummaryResponse> searchByTitle(String keyword) {
        return newsRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NewsSummaryResponse> getNewsByCategory(Long categoryId) {
        return newsRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    @Transactional
    public NewsDetailResponse updateNews(Long id, NewsUpdateRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News article not found with id: " + id));

        news.setTitle(request.getTitle().trim());
        news.setContent(request.getContent().trim());
        news.setAuthor(request.getAuthor().trim());
        if (request.getActive() != null) {
            news.setActive(request.getActive());
        }

        if (request.getCategoryId() != null && !request.getCategoryId().equals(news.getCategory().getId())) {
            Category newCategory = categoryService.getRequired(request.getCategoryId());
            news.setCategory(newCategory);
        }

        if (request.getTagNames() != null) {
            Set<Tag> newTags = new HashSet<>();
            for (String tagName : request.getTagNames()) {
                if (tagName != null && !tagName.isBlank()) {
                    newTags.add(tagService.getOrCreateTag(tagName));
                }
            }
            news.setTags(newTags);
        }

        // Dirty checking automatically persists changes when transaction commits
        return toDetailResponse(news);
    }

    @Transactional
    public void moveCategory(Long newsId, Long targetCategoryId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News article not found with id: " + newsId));
        Category targetCategory = categoryService.getRequired(targetCategoryId);
        if (!targetCategory.isActive()) {
            throw new IllegalArgumentException("Target category is inactive");
        }
        news.setCategory(targetCategory);
        // Demonstrating dirty checking: No explicit newsRepository.save(news) is necessary!
    }

    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News article not found with id: " + id));
        newsRepository.delete(news);
    }

    public NewsSummaryResponse toSummaryResponse(News news) {
        return NewsSummaryResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .categoryName(news.getCategory() != null ? news.getCategory().getName() : "Unassigned")
                .tagNames(news.getTags() != null
                        ? news.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
                        : Set.of())
                .author(news.getAuthor())
                .active(news.isActive())
                .publishedAt(news.getPublishedAt())
                .build();
    }

    public NewsDetailResponse toDetailResponse(News news) {
        return NewsDetailResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .author(news.getAuthor())
                .categoryId(news.getCategory() != null ? news.getCategory().getId() : null)
                .categoryName(news.getCategory() != null ? news.getCategory().getName() : null)
                .tags(news.getTags() != null
                        ? news.getTags().stream().map(tagService::toResponse).collect(Collectors.toSet())
                        : Set.of())
                .active(news.isActive())
                .publishedAt(news.getPublishedAt())
                .build();
    }
}
