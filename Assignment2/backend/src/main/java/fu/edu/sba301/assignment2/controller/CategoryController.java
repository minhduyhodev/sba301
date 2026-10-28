package fu.edu.sba301.assignment2.controller;

import fu.edu.sba301.assignment2.dto.CategoryRequest;
import fu.edu.sba301.assignment2.dto.CategoryResponse;
import fu.edu.sba301.assignment2.exception.BadRequestException;
import fu.edu.sba301.assignment2.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private void checkStaffOrAdminRole(Integer userRole) {
        if (userRole != null && userRole != 1 && userRole != 2) {
            throw new BadRequestException("Access denied: Staff (2) or Admin (1) role required.");
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(categoryService.searchCategories(keyword));
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffOrAdminRole(userRole);
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryRequest request,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffOrAdminRole(userRole);
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Integer id,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffOrAdminRole(userRole);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
