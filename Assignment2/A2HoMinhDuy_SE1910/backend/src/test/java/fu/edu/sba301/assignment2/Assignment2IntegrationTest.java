package fu.edu.sba301.assignment2;

import com.fasterxml.jackson.databind.ObjectMapper;
import fu.edu.sba301.assignment2.domain.Category;
import fu.edu.sba301.assignment2.domain.SystemAccount;
import fu.edu.sba301.assignment2.domain.Tag;
import fu.edu.sba301.assignment2.dto.*;
import fu.edu.sba301.assignment2.repository.CategoryRepository;
import fu.edu.sba301.assignment2.repository.NewsArticleRepository;
import fu.edu.sba301.assignment2.repository.SystemAccountRepository;
import fu.edu.sba301.assignment2.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class Assignment2IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SystemAccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NewsArticleRepository newsArticleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("1. Guest can view active news (NewsStatus = 1) without authentication")
    void testPublicNewsOnlyActive() throws Exception {
        mockMvc.perform(get("/api/news/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].newsStatus", everyItem(is(true))));
    }

    @Test
    @DisplayName("2. Authentication: Admin and Staff login with email and password")
    void testLoginSuccessAndFailure() throws Exception {
        // Valid Admin login
        LoginRequest adminLogin = new LoginRequest("admin@fpt.edu.vn", "admin123");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountRole", is(1)))
                .andExpect(jsonPath("$.roleName", is("ADMIN")));

        // Valid Staff login
        LoginRequest staffLogin = new LoginRequest("staff@fpt.edu.vn", "staff123");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staffLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountRole", is(2)))
                .andExpect(jsonPath("$.roleName", is("STAFF")));

        // Invalid credentials
        LoginRequest wrongLogin = new LoginRequest("admin@fpt.edu.vn", "wrongpassword");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid email or password")));
    }

    @Test
    @DisplayName("3. Admin: Account CRUD and delete restriction (Cannot delete if created news articles)")
    void testAccountCRUDAndConstraint() throws Exception {
        // Find staff1 (who created NA0001)
        SystemAccount staff1 = accountRepository.findByAccountEmail("staff@fpt.edu.vn").orElseThrow();

        // 3a. Trying to delete staff1 MUST fail because staff1 created articles
        mockMvc.perform(delete("/api/accounts/" + staff1.getAccountId())
                        .header("X-User-Role", 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Cannot delete account because it has already created news articles")));

        // 3b. Creating and deleting an account without articles MUST succeed
        AccountRequest newAcc = new AccountRequest("Temp Staff", "temp_staff_" + System.currentTimeMillis() + "@fpt.edu.vn", 2, "pass123");
        String createRes = mockMvc.perform(post("/api/accounts")
                        .header("X-User-Role", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAcc)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId", notNullValue()))
                .andReturn().getResponse().getContentAsString();

        AccountResponse createdAccount = objectMapper.readValue(createRes, AccountResponse.class);

        // Delete the new account (no articles created)
        mockMvc.perform(delete("/api/accounts/" + createdAccount.getAccountId())
                        .header("X-User-Role", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("4. Staff: Category CRUD and delete restriction (Cannot delete if in use by news articles)")
    void testCategoryCRUDAndConstraint() throws Exception {
        // Category "Technology & Engineering" is used by NA0001
        Category catTech = categoryRepository.findByCategoryNameContainingIgnoreCase("Technology").get(0);

        // 4a. Deleting used category MUST fail
        mockMvc.perform(delete("/api/categories/" + catTech.getCategoryId())
                        .header("X-User-Role", 2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Cannot delete category because it is currently used by news articles")));

        // 4b. Deleting unused category MUST succeed
        Category catUnused = categoryRepository.findByCategoryNameContainingIgnoreCase("Archived Notices").get(0);
        mockMvc.perform(delete("/api/categories/" + catUnused.getCategoryId())
                        .header("X-User-Role", 2))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("5. Staff: NewsArticle CRUD with tags and staff history")
    void testNewsArticleCRUDAndHistory() throws Exception {
        SystemAccount staff = accountRepository.findByAccountEmail("staff@fpt.edu.vn").orElseThrow();
        Category cat = categoryRepository.findByCategoryNameContainingIgnoreCase("Academic").get(0);
        Tag tag = tagRepository.findAll().get(0);

        // 5a. Create news
        String uniqueId = "TEST_NA_" + System.currentTimeMillis();
        NewsArticleRequest req = new NewsArticleRequest();
        req.setNewsArticleId(uniqueId);
        req.setNewsTitle("Automated Integration Test Article");
        req.setHeadline("Testing headline validation");
        req.setNewsContent("Comprehensive content for integration testing of news articles with Spring Boot 3.");
        req.setNewsSource("Integration Test Suite");
        req.setCategoryId(cat.getCategoryId());
        req.setNewsStatus(true);
        req.setCreatedById(staff.getAccountId());
        req.setTagIds(List.of(tag.getTagId()));

        mockMvc.perform(post("/api/news")
                        .header("X-User-Id", staff.getAccountId())
                        .header("X-User-Role", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.newsArticleId", is(uniqueId)))
                .andExpect(jsonPath("$.tags", hasSize(1)));

        // 5b. Fetch history of this staff
        mockMvc.perform(get("/api/news/history")
                        .header("X-User-Id", staff.getAccountId())
                        .header("X-User-Role", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].newsArticleId", hasItem(uniqueId)));

        // 5c. Delete the test news article
        mockMvc.perform(delete("/api/news/" + uniqueId)
                        .header("X-User-Role", 2))
                .andExpect(status().isNoContent());
    }
}
