package com.fpt.sba301.slot17.repository;

import com.fpt.sba301.slot17.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should find category ignoring case")
    void shouldFindCategoryIgnoringCase() {
        categoryRepository.save(new Category("Technology", "Tech news", true));

        Optional<Category> result = categoryRepository.findByNameIgnoreCase("technology");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Technology");
    }

    @Test
    @DisplayName("Should find active categories ordered by name ascending")
    void shouldFindActiveCategoriesOrderedByNameAsc() {
        categoryRepository.save(new Category("University", "Desc", true));
        categoryRepository.save(new Category("Alumni", "Desc", true));
        categoryRepository.save(new Category("Archive", "Desc", false));

        List<Category> activeList = categoryRepository.findByActiveTrueOrderByNameAsc();

        assertThat(activeList).hasSize(2);
        assertThat(activeList.get(0).getName()).isEqualTo("Alumni");
        assertThat(activeList.get(1).getName()).isEqualTo("University");
    }

    @Test
    @DisplayName("Should check existence of category by name ignoring case")
    void shouldCheckExistsByNameIgnoreCase() {
        categoryRepository.save(new Category("Sports", "Desc", true));

        assertThat(categoryRepository.existsByNameIgnoreCase("SPORTS")).isTrue();
        assertThat(categoryRepository.existsByNameIgnoreCase("Music")).isFalse();
    }
}
