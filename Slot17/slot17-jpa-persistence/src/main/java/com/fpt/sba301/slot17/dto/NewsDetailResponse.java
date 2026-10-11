package com.fpt.sba301.slot17.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDetailResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Long categoryId;
    private String categoryName;
    private Set<TagResponse> tags;
    private boolean active;
    private LocalDateTime publishedAt;
}
