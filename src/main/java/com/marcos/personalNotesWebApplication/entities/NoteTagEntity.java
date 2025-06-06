package com.marcos.personalNotesWebApplication.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "note_tags")
@Table(name = "note_tags",
       indexes = {
               @Index(name = "idx_note_tags_note_id", columnList = "note_id"),
               @Index(name = "idx_note_tags_tag_id", columnList = "tag_id"),
               @Index(name = "idx_note_tags_created_at", columnList = "created_at"),
               @Index(name = "idx_note_tags_note_tag", columnList = "note_id, tag_id")
       },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_note_tag", columnNames = {"note_id", "tag_id"})
        })
@EntityListeners(AuditingEntityListener.class)
public class NoteTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private NoteEntity note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteTagEntity)) return false;
        NoteTagEntity that = (NoteTagEntity) o;
        return note != null && note.equals(that.getNote()) &&
                tag != null && tag.equals(that.getTag());
    }

    @Override
    public int hashCode() {
        int result = note != null ? note.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NoteTagEntity{" +
                "id=" + id +
                ", noteId=" + (note != null ? note.getId() : null) +
                ", tagName=" + (tag != null ? tag.getName() : null) +
                ", createdAt=" + createdAt +
                '}';
    }
}
