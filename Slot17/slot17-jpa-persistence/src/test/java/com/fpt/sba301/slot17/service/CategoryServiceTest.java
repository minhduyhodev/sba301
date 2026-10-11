package com.fpt.sba301.slot17.service;

import com.fpt.sba301.slot17.dto.CategoryRequest;
import com.fpt.sba301.slot17.dto.CategoryResponse;
import com.fpt.sba301.slot17.exception.DuplicateResourceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("Should create category successfully")
    void shouldCreateCategorySuccessfully() {
        CategoryRequest req = new CategoryRequest("Research", "Academic research", true);
        CategoryResponse response = categoryService.createCategory(req);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("Research");
        assertThat(response.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should reject category with blank name")
    void shouldRejectBlankCategoryName() {
        CategoryRequest req = new CategoryRequest("   ", "Description", true);

        assertThatThrownBy(() -> categoryService.createCategory(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("required");
    }

    @Test
    @DisplayName("Should reject duplicate category name (case-insensitive business rule)")
    void shouldRejectDuplicateCategoryName() {
        categoryService.createCategory(new CategoryRequest("Innovation", "Desc", true));

        CategoryRequest duplicateReq = new CategoryRequest("innovation", "Other desc", true);

        assertThatThrownBy(() -> categoryService.createCategory(duplicateReq))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }
}
