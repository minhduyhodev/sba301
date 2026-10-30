package fu.edu.sba301.assignment2.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "NewsArticle")
public class NewsArticle {

    @Id
    @Column(name = "NewsArticleID", length = 50)
    private String newsArticleId;

    @Column(name = "NewsTitle", nullable = false, length = 255)
    private String newsTitle;

    @Column(name = "Headline", length = 255)
    private String headline;

    @Column(name = "CreatedDate", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "NewsContent", nullable = false, length = 4000)
    private String newsContent;

    @Column(name = "NewsSource", length = 100)
    private String newsSource;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CategoryID", nullable = false)
    private Category category;

    @Column(name = "NewsStatus", nullable = false)
    private Boolean newsStatus = true; // 1 = active, 0 = inactive

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CreatedByID", nullable = false)
    private SystemAccount createdBy;

    @Column(name = "UpdatedByID")
    private Integer updatedById;

    @Column(name = "ModifiedDate")
    private LocalDateTime modifiedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "NewsTag",
        joinColumns = @JoinColumn(name = "NewsArticleID"),
        inverseJoinColumns = @JoinColumn(name = "TagID")
    )
    private Set<Tag> tags = new HashSet<>();

    public NewsArticle() {
        this.createdDate = LocalDateTime.now();
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getNewsStatus() {
        return newsStatus;
    }

    public void setNewsStatus(Boolean newsStatus) {
        this.newsStatus = newsStatus;
    }

    public SystemAccount getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SystemAccount createdBy) {
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsArticle that)) return false;
        return Objects.equals(newsArticleId, that.newsArticleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsArticleId);
    }
}
