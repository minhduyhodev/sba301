package fu.edu.sba301.assignment2.repository;

import fu.edu.sba301.assignment2.domain.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, String> {

    // Constraint checks
    boolean existsByCreatedBy_AccountId(Integer accountId);
    boolean existsByCategory_CategoryId(Integer categoryId);

    // Public active news (NewsStatus = 1)
    List<NewsArticle> findByNewsStatusTrueOrderByCreatedDateDesc();

    // Staff history: news created by current staff
    List<NewsArticle> findByCreatedBy_AccountIdOrderByCreatedDateDesc(Integer accountId);

    // Search for public active news by keyword (title/content) or category
    @Query("SELECT n FROM NewsArticle n WHERE n.newsStatus = true AND " +
           "(:keyword IS NULL OR LOWER(n.newsTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.newsContent) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR n.category.categoryId = :categoryId) " +
           "ORDER BY n.createdDate DESC")
    List<NewsArticle> searchPublicNews(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId);

    // Search for staff management (all statuses)
    @Query("SELECT n FROM NewsArticle n WHERE " +
           "(:keyword IS NULL OR LOWER(n.newsTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.newsContent) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR n.category.categoryId = :categoryId) " +
           "ORDER BY n.createdDate DESC")
    List<NewsArticle> searchAllNews(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId);
}
