package fu.edu.sba301.assignment2.controller;

import fu.edu.sba301.assignment2.dto.NewsArticleRequest;
import fu.edu.sba301.assignment2.dto.NewsArticleResponse;
import fu.edu.sba301.assignment2.exception.BadRequestException;
import fu.edu.sba301.assignment2.service.NewsArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsArticleController {

    private final NewsArticleService newsArticleService;

    public NewsArticleController(NewsArticleService newsArticleService) {
        this.newsArticleService = newsArticleService;
    }

    private void checkStaffRole(Integer userRole) {
        if (userRole != null && userRole != 2 && userRole != 1) {
            throw new BadRequestException("Access denied: Staff role (2) required to manage news.");
        }
    }

    // 1. PUBLIC ENDPOINTS (No authentication required)
    @GetMapping("/public")
    public ResponseEntity<List<NewsArticleResponse>> getPublicNews(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId) {
        return ResponseEntity.ok(newsArticleService.getPublicActiveNews(keyword, categoryId));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<NewsArticleResponse> getPublicNewsById(@PathVariable String id) {
        NewsArticleResponse article = newsArticleService.getNewsById(id);
        if (!Boolean.TRUE.equals(article.getNewsStatus())) {
            throw new BadRequestException("This article is inactive and not available publicly.");
        }
        return ResponseEntity.ok(article);
    }

    // 2. STAFF ENDPOINTS
    @GetMapping
    public ResponseEntity<List<NewsArticleResponse>> getAllNewsForStaff(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffRole(userRole);
        return ResponseEntity.ok(newsArticleService.getAllNewsForStaff(keyword, categoryId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<NewsArticleResponse>> getNewsHistory(
            @RequestParam(required = false) Integer staffId,
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffRole(userRole);
        Integer targetStaffId = staffId != null ? staffId : userId;
        if (targetStaffId == null) {
            throw new BadRequestException("Staff ID is required to fetch news history.");
        }
        return ResponseEntity.ok(newsArticleService.getNewsHistoryByStaff(targetStaffId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsArticleResponse> getNewsById(
            @PathVariable String id,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffRole(userRole);
        return ResponseEntity.ok(newsArticleService.getNewsById(id));
    }

    @PostMapping
    public ResponseEntity<NewsArticleResponse> createNews(
            @Valid @RequestBody NewsArticleRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffRole(userRole);
        NewsArticleResponse response = newsArticleService.createNews(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsArticleResponse> updateNews(
            @PathVariable String id,
            @Valid @RequestBody NewsArticleRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Integer userId,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffRole(userRole);
        NewsArticleResponse response = newsArticleService.updateNews(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(
            @PathVariable String id,
            @RequestHeader(value = "X-User-Role", required = false) Integer userRole) {
        checkStaffRole(userRole);
        newsArticleService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}
