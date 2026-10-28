package fu.edu.sba301.assignment2.service;

import fu.edu.sba301.assignment2.domain.Category;
import fu.edu.sba301.assignment2.dto.CategoryRequest;
import fu.edu.sba301.assignment2.dto.CategoryResponse;
import fu.edu.sba301.assignment2.exception.BadRequestException;
import fu.edu.sba301.assignment2.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment2.repository.CategoryRepository;
import fu.edu.sba301.assignment2.repository.NewsArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final NewsArticleRepository newsArticleRepository;

    public CategoryService(CategoryRepository categoryRepository, NewsArticleRepository newsArticleRepository) {
        this.categoryRepository = categoryRepository;
        this.newsArticleRepository = newsArticleRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> searchCategories(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCategories();
        }
        return categoryRepository.findByCategoryNameContainingIgnoreCase(keyword.trim()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return toResponse(category);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setCategoryName(request.getCategoryName().trim());
        category.setCategoryDesciption(request.getCategoryDesciption());
        category.setParentCategoryId(request.getParentCategoryId());
        category.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Avoid cyclic parent reference
        if (request.getParentCategoryId() != null && request.getParentCategoryId().equals(id)) {
            throw new BadRequestException("Category cannot be its own parent");
        }

        category.setCategoryName(request.getCategoryName().trim());
        category.setCategoryDesciption(request.getCategoryDesciption());
        category.setParentCategoryId(request.getParentCategoryId());
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }

        Category updated = categoryRepository.save(category);
        return toResponse(updated);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        // Rule: The delete action will delete an item in the case this item is not belong to any news articles.
        // If the item is already stored in a news article cannot delete.
        if (newsArticleRepository.existsByCategory_CategoryId(id)) {
            throw new BadRequestException("Cannot delete category because it is currently used by news articles.");
        }

        if (categoryRepository.existsByParentCategoryId(id)) {
            throw new BadRequestException("Cannot delete category because other categories reference it as parent category.");
        }

        categoryRepository.deleteById(id);
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getCategoryDesciption(),
                category.getParentCategoryId(),
                category.getIsActive()
        );
    }
}
