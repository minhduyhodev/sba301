package com.fpt.sba301.slot17.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    // Inverse side of Bidirectional One-to-Many
    // mappedBy points to the 'category' field in News entity (Owning side)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<News> newsItems = new ArrayList<>();

    protected Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    public Category(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }

    // Helper methods to keep both sides of the association synchronized
    public void addNews(News news) {
        newsItems.add(news);
        news.setCategory(this);
    }

    public void removeNews(News news) {
        newsItems.remove(news);
        news.setCategory(null);
    }
}
