package fu.edu.sba301.assignment2.config;

import fu.edu.sba301.assignment2.domain.Category;
import fu.edu.sba301.assignment2.domain.NewsArticle;
import fu.edu.sba301.assignment2.domain.SystemAccount;
import fu.edu.sba301.assignment2.domain.Tag;
import fu.edu.sba301.assignment2.repository.CategoryRepository;
import fu.edu.sba301.assignment2.repository.NewsArticleRepository;
import fu.edu.sba301.assignment2.repository.SystemAccountRepository;
import fu.edu.sba301.assignment2.repository.TagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SystemAccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final NewsArticleRepository newsArticleRepository;

    public DataInitializer(SystemAccountRepository accountRepository,
                           CategoryRepository categoryRepository,
                           TagRepository tagRepository,
                           NewsArticleRepository newsArticleRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.newsArticleRepository = newsArticleRepository;
    }

    @Override
    public void run(String... args) {
        if (accountRepository.count() > 0) {
            return;
        }

        // 1. Seed Accounts
        SystemAccount admin = new SystemAccount(null, "System Administrator", "admin@fpt.edu.vn", 1, "admin123");
        SystemAccount staff1 = new SystemAccount(null, "Ho Minh Duy", "staff@fpt.edu.vn", 2, "staff123");
        SystemAccount staff2 = new SystemAccount(null, "Test Staff (No Articles)", "staff_idle@fpt.edu.vn", 2, "staff123");

        admin = accountRepository.save(admin);
        staff1 = accountRepository.save(staff1);
        staff2 = accountRepository.save(staff2);

        // 2. Seed Categories
        Category catAcademic = new Category(null, "Academic Affairs", "Academic policies and curriculum updates", null, true);
        Category catCampus = new Category(null, "Campus Life", "Events, clubs, and extracurricular activities", null, true);
        Category catTech = new Category(null, "Technology & Engineering", "Latest updates in IT, Software, and AI", null, true);
        Category catUnused = new Category(null, "Archived Notices", "Used to demonstrate deletion without existing articles", null, true);

        catAcademic = categoryRepository.save(catAcademic);
        catCampus = categoryRepository.save(catCampus);
        catTech = categoryRepository.save(catTech);
        catUnused = categoryRepository.save(catUnused);

        // 3. Seed Tags
        Tag tagJava = tagRepository.save(new Tag("Java", "Java ecosystem and runtime"));
        Tag tagSpring = tagRepository.save(new Tag("Spring Boot", "Enterprise Spring Framework"));
        Tag tagReact = tagRepository.save(new Tag("ReactJS", "Frontend UI library"));
        Tag tagFPT = tagRepository.save(new Tag("FPT University", "FPT campus community"));

        // 4. Seed News Articles
        // Article 1: Active
        NewsArticle article1 = new NewsArticle();
        article1.setNewsArticleId("NA0001");
        article1.setNewsTitle("FPT University Hosts Annual Tech Showcase 2026");
        article1.setHeadline("Leading technology students present cutting-edge web & AI projects.");
        article1.setNewsContent("FPT University successfully kicked off its Tech Showcase 2026, featuring over 100 student projects using Spring Boot 3, ReactJS, and Cloud Native solutions. Industry experts praised the practical engineering approach.");
        article1.setNewsSource("FPT Campus News");
        article1.setCategory(catTech);
        article1.setNewsStatus(true);
        article1.setCreatedBy(staff1);
        article1.setCreatedDate(LocalDateTime.now().minusDays(2));
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(tagJava);
        tags1.add(tagSpring);
        tags1.add(tagReact);
        article1.setTags(tags1);
        newsArticleRepository.save(article1);

        // Article 2: Active
        NewsArticle article2 = new NewsArticle();
        article2.setNewsArticleId("NA0002");
        article2.setNewsTitle("Course Registration Schedule for Summer 2026 Semester");
        article2.setHeadline("All SE and IA students must finalize course choices before deadline.");
        article2.setNewsContent("The Academic Department announces the official opening of course registration. Students are requested to check prerequisite courses such as SBA301 and SWP391 before submitting enrollment forms.");
        article2.setNewsSource("Academic Department");
        article2.setCategory(catAcademic);
        article2.setNewsStatus(true);
        article2.setCreatedBy(staff1);
        article2.setCreatedDate(LocalDateTime.now().minusDays(1));
        Set<Tag> tags2 = new HashSet<>();
        tags2.add(tagFPT);
        article2.setTags(tags2);
        newsArticleRepository.save(article2);

        // Article 3: Inactive (Draft - only visible to staff, not public)
        NewsArticle article3 = new NewsArticle();
        article3.setNewsArticleId("NA0003");
        article3.setNewsTitle("[Draft] Scheduled Maintenance for Campus Wi-Fi Network");
        article3.setHeadline("Internal notification for upcoming weekend network upgrade.");
        article3.setNewsContent("This draft article is currently under review by IT operations. NewsStatus is set to 0 (Inactive) to verify that unauthenticated visitors cannot view it.");
        article3.setNewsSource("Internal Operations");
        article3.setCategory(catCampus);
        article3.setNewsStatus(false); // Inactive
        article3.setCreatedBy(staff1);
        article3.setCreatedDate(LocalDateTime.now());
        Set<Tag> tags3 = new HashSet<>();
        tags3.add(tagFPT);
        article3.setTags(tags3);
        newsArticleRepository.save(article3);
    }
}
