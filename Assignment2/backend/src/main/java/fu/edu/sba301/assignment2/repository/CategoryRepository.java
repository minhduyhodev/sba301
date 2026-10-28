package fu.edu.sba301.assignment2.repository;

import fu.edu.sba301.assignment2.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);
    List<Category> findByIsActiveTrue();
    boolean existsByParentCategoryId(Integer parentCategoryId);
}
