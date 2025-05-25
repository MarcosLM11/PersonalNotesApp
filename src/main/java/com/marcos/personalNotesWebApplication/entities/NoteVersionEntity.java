package com.marcos.personalNotesWebApplication.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "note_versions")
@Table(name = "note_versions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"note_id", "version"})
})
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "note")
@EqualsAndHashCode(of = "id")
public class NoteVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private NoteEntity note;

    @Min(1)
    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedDate
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
