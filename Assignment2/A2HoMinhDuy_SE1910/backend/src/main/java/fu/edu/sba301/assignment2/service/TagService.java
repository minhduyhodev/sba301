package fu.edu.sba301.assignment2.service;

import fu.edu.sba301.assignment2.domain.Tag;
import fu.edu.sba301.assignment2.dto.TagDto;
import fu.edu.sba301.assignment2.exception.BadRequestException;
import fu.edu.sba301.assignment2.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment2.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TagDto> searchTags(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTags();
        }
        return tagRepository.findByTagNameContainingIgnoreCase(keyword.trim()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TagDto getTagById(Integer id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return toDto(tag);
    }

    @Transactional
    public TagDto createTag(TagDto dto) {
        if (tagRepository.findByTagNameIgnoreCase(dto.getTagName().trim()).isPresent()) {
            throw new BadRequestException("Tag name already exists: " + dto.getTagName());
        }

        Tag tag = new Tag();
        tag.setTagName(dto.getTagName().trim());
        tag.setNote(dto.getNote());

        Tag saved = tagRepository.save(tag);
        return toDto(saved);
    }

    @Transactional
    public TagDto updateTag(Integer id, TagDto dto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        String newName = dto.getTagName().trim();
        tagRepository.findByTagNameIgnoreCase(newName).ifPresent(existing -> {
            if (!existing.getTagId().equals(id)) {
                throw new BadRequestException("Tag name already exists: " + newName);
            }
        });

        tag.setTagName(newName);
        tag.setNote(dto.getNote());

        Tag updated = tagRepository.save(tag);
        return toDto(updated);
    }

    @Transactional
    public void deleteTag(Integer id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }

    public TagDto toDto(Tag tag) {
        return new TagDto(tag.getTagId(), tag.getTagName(), tag.getNote());
    }
}
