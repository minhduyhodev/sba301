package fu.edu.sba301.assignment2.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TagID")
    private Integer tagId;

    @Column(name = "TagName", nullable = false, length = 100)
    private String tagName;

    @Column(name = "Note", length = 255)
    private String note;

    public Tag() {
    }

    public Tag(Integer tagId, String tagName, String note) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.note = note;
    }

    public Tag(String tagName, String note) {
        this.tagName = tagName;
        this.note = note;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(tagId, tag.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId);
    }
}
