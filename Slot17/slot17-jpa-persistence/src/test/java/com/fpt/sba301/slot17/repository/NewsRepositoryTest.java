package com.fpt.sba301.slot17.repository;

import com.fpt.sba301.slot17.domain.Category;
import com.fpt.sba301.slot17.domain.News;
import com.fpt.sba301.slot17.domain.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("Should save news with category and tags, and query by category")
    void shouldSaveAndQueryNewsWithCategoryAndTags() {
        Category cat = categoryRepository.save(new Category("Campus", "Campus life", true));
        Tag tagJava = tagRepository.save(new Tag("Java"));

        News article = new News("Spring Boot 3 Workshop", "Details about workshop", "Admin", cat);
        article.addTag(tagJava);
        News saved = newsRepository.save(article);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCategory().getId()).isEqualTo(cat.getId());
        assertThat(saved.getTags()).hasSize(1);

        List<News> byCat = newsRepository.findByCategoryId(cat.getId());
        assertThat(byCat).hasSize(1);
        assertThat(byCat.get(0).getTitle()).isEqualTo("Spring Boot 3 Workshop");

        List<News> byTitle = newsRepository.findByTitleContainingIgnoreCase("workshop");
        assertThat(byTitle).hasSize(1);
    }
}
