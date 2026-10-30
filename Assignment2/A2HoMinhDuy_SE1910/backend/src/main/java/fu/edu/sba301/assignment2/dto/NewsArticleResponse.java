package fu.edu.sba301.assignment2.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewsArticleResponse {

    private String newsArticleId;
    private String newsTitle;
    private String headline;
    private LocalDateTime createdDate;
    private String newsContent;
    private String newsSource;
    private CategoryResponse category;
    private Boolean newsStatus;
    private AccountResponse createdBy;
    private Integer updatedById;
    private LocalDateTime modifiedDate;
    private List<TagDto> tags = new ArrayList<>();

    public NewsArticleResponse() {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public Boolean getNewsStatus() {
        return newsStatus;
    }

    public void setNewsStatus(Boolean newsStatus) {
        this.newsStatus = newsStatus;
    }

    public AccountResponse getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AccountResponse createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Integer updatedById) {
        this.updatedById = updatedById;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
    }
}
