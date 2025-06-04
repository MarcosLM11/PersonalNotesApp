package com.marcos.personalNotesWebApplication.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "notes")
@Table(name = "notes", indexes = {
    @Index(name = "idx_notes_author_id", columnList = "author_id"),
    @Index(name = "idx_notes_created_at", columnList = "created_at"),
    @Index(name = "idx_notes_updated_at", columnList = "updated_at"),
    @Index(name = "idx_notes_is_public", columnList = "is_public"),
    @Index(name = "idx_notes_author_created", columnList = "author_id, created_at"),
    @Index(name = "idx_notes_public_created", columnList = "is_public, created_at")
})
@EntityListeners(AuditingEntityListener.class)
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Size(max = 200)
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Column(name = "s3_url")
    private String s3Url;

    @Size(max = 500)
    @Column(name = "tags")
    private String tags;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoteVersionEntity> versions = new ArrayList<>();

    public void addVersion(NoteVersionEntity version) {
        versions.add(version);
        version.setNote(this);
        version.setVersion(versions.size());
        version.setContent(this.content);
    }

    public void removeVersion(NoteVersionEntity version) {
        versions.remove(version);
        version.setNote(null);
    }
}
