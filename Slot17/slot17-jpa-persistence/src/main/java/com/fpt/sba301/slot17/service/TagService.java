package com.fpt.sba301.slot17.service;

import com.fpt.sba301.slot17.domain.Tag;
import com.fpt.sba301.slot17.dto.TagRequest;
import com.fpt.sba301.slot17.dto.TagResponse;
import com.fpt.sba301.slot17.exception.DuplicateResourceException;
import com.fpt.sba301.slot17.exception.ResourceNotFoundException;
import com.fpt.sba301.slot17.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public TagResponse createTag(TagRequest request) {
        String normalized = request.getName() == null ? "" : request.getName().trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Tag name is required");
        }
        if (tagRepository.existsByNameIgnoreCase(normalized)) {
            throw new DuplicateResourceException("Tag already exists: " + normalized);
        }
        Tag tag = new Tag(normalized);
        Tag saved = tagRepository.save(tag);
        return toResponse(saved);
    }

    @Transactional
    public Tag getOrCreateTag(String name) {
        String normalized = name.trim();
        return tagRepository.findByNameIgnoreCase(normalized)
                .orElseGet(() -> tagRepository.save(new Tag(normalized)));
    }

    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Tag getRequired(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
    }

    public TagResponse toResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
