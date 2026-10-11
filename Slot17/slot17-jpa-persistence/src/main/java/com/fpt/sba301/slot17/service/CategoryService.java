package com.fpt.sba301.slot17.service;

import com.fpt.sba301.slot17.domain.Category;
import com.fpt.sba301.slot17.dto.CategoryRequest;
import com.fpt.sba301.slot17.dto.CategoryResponse;
import com.fpt.sba301.slot17.exception.DuplicateResourceException;
import com.fpt.sba301.slot17.exception.ResourceNotFoundException;
import com.fpt.sba301.slot17.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        String normalized = request.getName() == null ? "" : request.getName().trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (categoryRepository.existsByNameIgnoreCase(normalized)) {
            throw new DuplicateResourceException("Category already exists with name: " + normalized);
        }

        Category category = new Category(normalized, request.getDescription(),
                request.getActive() != null ? request.getActive() : true);
        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Category getRequired(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        return toResponse(getRequired(id));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = getRequired(id);

        String normalized = request.getName() == null ? "" : request.getName().trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }

        if (!category.getName().equalsIgnoreCase(normalized) &&
                categoryRepository.existsByNameIgnoreCase(normalized)) {
            throw new DuplicateResourceException("Category name already exists: " + normalized);
        }

        // Dirty checking: Hibernate detects changes and triggers UPDATE automatically at commit
        category.setName(normalized);
        category.setDescription(request.getDescription());
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        return toResponse(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getRequired(id);
        categoryRepository.delete(category);
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.isActive())
                .newsCount(category.getNewsItems() != null ? category.getNewsItems().size() : 0)
                .build();
    }
}
