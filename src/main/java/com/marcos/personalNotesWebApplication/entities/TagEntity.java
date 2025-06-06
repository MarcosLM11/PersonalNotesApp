package com.marcos.personalNotesWebApplication.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.Builder.Default;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tags")
@Table(name = "tags",
indexes = {
        @Index(name = "idx_tags_name", columnList = "name", unique = true),
        @Index(name = "idx_tags_usage_count", columnList = "usage_count"),
        @Index(name = "idx_tags_created_at", columnList = "created_at")
        })
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @NotBlank
    @Size(min=1, max = 50)
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color")
    @Column(name = "color", length = 7)
    @Default
    private String color = "#3B82F6"; //Blue color as default
    @Column(name = "usage_count", nullable = false)
    @Default
    private Long usageCount = 0L;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NoteTagEntity> noteTags = new ArrayList<>();

    public void incrementUsageCount() {
        this.usageCount++;
    }

    public void decrementUsageCount() {
        if (this.usageCount > 0) {
            this.usageCount--;
        }
    }

    @PrePersist
    @PreUpdate
    private void validateAndNormalize() {
        if (name != null) {
            this.name = name.trim().toLowerCase();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagEntity)) return false;
        TagEntity tagEntity = (TagEntity) o;
        return name != null && name.equals(tagEntity.getName());
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TagEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usageCount=" + usageCount +
                '}';
    }
}
