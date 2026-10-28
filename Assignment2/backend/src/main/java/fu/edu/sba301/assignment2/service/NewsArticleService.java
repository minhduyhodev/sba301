package fu.edu.sba301.assignment2.service;

import fu.edu.sba301.assignment2.domain.Category;
import fu.edu.sba301.assignment2.domain.NewsArticle;
import fu.edu.sba301.assignment2.domain.SystemAccount;
import fu.edu.sba301.assignment2.domain.Tag;
import fu.edu.sba301.assignment2.dto.*;
import fu.edu.sba301.assignment2.exception.BadRequestException;
import fu.edu.sba301.assignment2.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment2.repository.CategoryRepository;
import fu.edu.sba301.assignment2.repository.NewsArticleRepository;
import fu.edu.sba301.assignment2.repository.SystemAccountRepository;
import fu.edu.sba301.assignment2.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NewsArticleService {

    private final NewsArticleRepository newsArticleRepository;
    private final CategoryRepository categoryRepository;
    private final SystemAccountRepository accountRepository;
    private final TagRepository tagRepository;

    public NewsArticleService(NewsArticleRepository newsArticleRepository,
                              CategoryRepository categoryRepository,
                              SystemAccountRepository accountRepository,
                              TagRepository tagRepository) {
        this.newsArticleRepository = newsArticleRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
        this.tagRepository = tagRepository;
    }

    public List<NewsArticleResponse> getPublicActiveNews(String keyword, Integer categoryId) {
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return newsArticleRepository.searchPublicNews(kw, categoryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<NewsArticleResponse> getAllNewsForStaff(String keyword, Integer categoryId) {
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return newsArticleRepository.searchAllNews(kw, categoryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<NewsArticleResponse> getNewsHistoryByStaff(Integer staffId) {
        return newsArticleRepository.findByCreatedBy_AccountIdOrderByCreatedDateDesc(staffId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NewsArticleResponse getNewsById(String id) {
        NewsArticle article = newsArticleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News article not found with id: " + id));
        return toResponse(article);
    }

    @Transactional
    public NewsArticleResponse createNews(NewsArticleRequest request, Integer currentUserId) {
        String articleId = request.getNewsArticleId();
        if (articleId == null || articleId.trim().isEmpty()) {
            articleId = "NA" + System.currentTimeMillis();
        } else {
            articleId = articleId.trim();
            if (newsArticleRepository.existsById(articleId)) {
                throw new BadRequestException("NewsArticleID already exists: " + articleId);
            }
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Integer creatorId = (currentUserId != null) ? currentUserId : request.getCreatedById();
        if (creatorId == null) {
            throw new BadRequestException("Creator account ID must be specified");
        }
        SystemAccount creator = accountRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator account not found with id: " + creatorId));

        NewsArticle article = new NewsArticle();
        article.setNewsArticleId(articleId);
        article.setNewsTitle(request.getNewsTitle().trim());
        article.setHeadline(request.getHeadline());
        article.setNewsContent(request.getNewsContent().trim());
        article.setNewsSource(request.getNewsSource());
        article.setCategory(category);
        article.setNewsStatus(request.getNewsStatus() != null ? request.getNewsStatus() : true);
        article.setCreatedBy(creator);
        article.setCreatedDate(LocalDateTime.now());

        // Attach tags
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            article.setTags(tags);
        }

        NewsArticle saved = newsArticleRepository.save(article);
        return toResponse(saved);
    }

    @Transactional
    public NewsArticleResponse updateNews(String id, NewsArticleRequest request, Integer currentUserId) {
        NewsArticle article = newsArticleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News article not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        article.setNewsTitle(request.getNewsTitle().trim());
        article.setHeadline(request.getHeadline());
        article.setNewsContent(request.getNewsContent().trim());
        article.setNewsSource(request.getNewsSource());
        article.setCategory(category);
        if (request.getNewsStatus() != null) {
            article.setNewsStatus(request.getNewsStatus());
        }

        if (currentUserId != null) {
            article.setUpdatedById(currentUserId);
        }
        article.setModifiedDate(LocalDateTime.now());

        // Update tags
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            article.setTags(tags);
        }

        NewsArticle updated = newsArticleRepository.save(article);
        return toResponse(updated);
    }

    @Transactional
    public void deleteNews(String id) {
        NewsArticle article = newsArticleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News article not found with id: " + id));

        // Clear tags association in junction table NewsTag
        article.getTags().clear();
        newsArticleRepository.save(article);

        newsArticleRepository.delete(article);
    }

    public NewsArticleResponse toResponse(NewsArticle article) {
        NewsArticleResponse res = new NewsArticleResponse();
        res.setNewsArticleId(article.getNewsArticleId());
        res.setNewsTitle(article.getNewsTitle());
        res.setHeadline(article.getHeadline());
        res.setCreatedDate(article.getCreatedDate());
        res.setNewsContent(article.getNewsContent());
        res.setNewsSource(article.getNewsSource());
        res.setNewsStatus(article.getNewsStatus());
        res.setUpdatedById(article.getUpdatedById());
        res.setModifiedDate(article.getModifiedDate());

        if (article.getCategory() != null) {
            Category c = article.getCategory();
            res.setCategory(new CategoryResponse(
                    c.getCategoryId(),
                    c.getCategoryName(),
                    c.getCategoryDesciption(),
                    c.getParentCategoryId(),
                    c.getIsActive()
            ));
        }

        if (article.getCreatedBy() != null) {
            SystemAccount a = article.getCreatedBy();
            res.setCreatedBy(new AccountResponse(
                    a.getAccountId(),
                    a.getAccountName(),
                    a.getAccountEmail(),
                    a.getAccountRole()
            ));
        }

        if (article.getTags() != null) {
            List<TagDto> tagDtos = article.getTags().stream()
                    .map(t -> new TagDto(t.getTagId(), t.getTagName(), t.getNote()))
                    .collect(Collectors.toList());
            res.setTags(tagDtos);
        }

        return res;
    }
}
