package fu.edu.sba301.assignment2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class NewsArticleRequest {

    private String newsArticleId;

    @NotBlank(message = "News title is required")
    @Size(max = 255, message = "News title cannot exceed 255 characters")
    private String newsTitle;

    @Size(max = 255, message = "Headline cannot exceed 255 characters")
    private String headline;

    @NotBlank(message = "News content is required")
    @Size(max = 4000, message = "Content cannot exceed 4000 characters")
    private String newsContent;

    @Size(max = 100, message = "Source cannot exceed 100 characters")
    private String newsSource;

    @NotNull(message = "Category is required")
    private Integer categoryId;

    private Boolean newsStatus = true; // 1 = active, 0 = inactive

    private Integer createdById;

    private List<Integer> tagIds = new ArrayList<>();

    public NewsArticleRequest() {
    }

    public String getNewsArticleId() {
        return newsArticleId;
    }

    public void setNewsArticleId(String newsArticleId) {
        this.newsArticleId = newsArticleId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getNewsStatus() {
        return newsStatus;
    }

    public void setNewsStatus(Boolean newsStatus) {
        this.newsStatus = newsStatus;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds != null ? tagIds : new ArrayList<>();
    }
}
