package com.fpt.sba301.slot17.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.sba301.slot17.dto.NewsCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/news should return page of news summaries without JSON recursion")
    void shouldReturnNewsPage() throws Exception {
        mockMvc.perform(get("/api/news?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$.content[0].categoryName").exists())
                .andExpect(jsonPath("$.content[0].tagNames").isArray());
    }

    @Test
    @DisplayName("POST /api/news should create news with tags and category")
    void shouldCreateNewsArticle() throws Exception {
        NewsCreateRequest req = new NewsCreateRequest(
                "New Java 21 LTS Features",
                "Virtual threads and pattern matching simplify concurrent backend code.",
                "Java Champion",
                1L,
                Set.of("Java", "Backend")
        );

        mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Java 21 LTS Features")))
                .andExpect(jsonPath("$.categoryName", is("Technology")))
                .andExpect(jsonPath("$.tags", hasSize(2)));
    }

    @Test
    @DisplayName("PATCH /api/news/{id}/move-category should move news category")
    void shouldMoveNewsCategory() throws Exception {
        mockMvc.perform(patch("/api/news/1/move-category?targetCategoryId=2"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/news/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId", is(2)))
                .andExpect(jsonPath("$.categoryName", is("Education")));
    }
}
