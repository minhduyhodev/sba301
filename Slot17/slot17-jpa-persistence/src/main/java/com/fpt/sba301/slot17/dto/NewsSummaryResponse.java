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
public class NewsSummaryResponse {
    private Long id;
    private String title;
    private String categoryName;
    private Set<String> tagNames;
    private String author;
    private boolean active;
    private LocalDateTime publishedAt;
}
