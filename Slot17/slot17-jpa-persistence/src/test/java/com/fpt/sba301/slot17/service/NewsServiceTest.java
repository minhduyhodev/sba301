package com.fpt.sba301.slot17.service;

import com.fpt.sba301.slot17.dto.CategoryRequest;
import com.fpt.sba301.slot17.dto.CategoryResponse;
import com.fpt.sba301.slot17.dto.NewsCreateRequest;
import com.fpt.sba301.slot17.dto.NewsDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("Should create news with category and tags, and demonstrate DTO boundary")
    void shouldCreateNewsWithCategoryAndTags() {
        CategoryResponse cat = categoryService.createCategory(
                new CategoryRequest("Science", "Scientific discovery", true));

        NewsCreateRequest req = new NewsCreateRequest(
                "Breakthrough in AI",
                "New AI algorithms improve software development speeds.",
                "Dr. John",
                cat.getId(),
                Set.of("AI", "Tech", "Science")
        );

        NewsDetailResponse detail = newsService.createNews(req);

        assertThat(detail.getId()).isNotNull();
        assertThat(detail.getTitle()).isEqualTo("Breakthrough in AI");
        assertThat(detail.getCategoryName()).isEqualTo("Science");
        assertThat(detail.getTags()).hasSize(3);
    }

    @Test
    @DisplayName("Should demonstrate Dirty Checking in moveCategory without explicit save")
    void shouldDemonstrateDirtyChecking() {
        CategoryResponse catA = categoryService.createCategory(new CategoryRequest("Dept A", "Desc", true));
        CategoryResponse catB = categoryService.createCategory(new CategoryRequest("Dept B", "Desc", true));

        NewsCreateRequest req = new NewsCreateRequest(
                "Department Notice", "Content body", "Admin", catA.getId(), Set.of()
        );
        NewsDetailResponse created = newsService.createNews(req);
        assertThat(created.getCategoryName()).isEqualTo("Dept A");

        // Execute moveCategory which mutates the entity in @Transactional method without save()
        newsService.moveCategory(created.getId(), catB.getId());

        // Verify that Hibernate flushed the update to database
        NewsDetailResponse reloaded = newsService.getNewsById(created.getId());
        assertThat(reloaded.getCategoryId()).isEqualTo(catB.getId());
        assertThat(reloaded.getCategoryName()).isEqualTo("Dept B");
    }
}
