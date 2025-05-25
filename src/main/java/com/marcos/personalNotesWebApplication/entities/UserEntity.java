package com.marcos.personalNotesWebApplication.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
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
@Entity(name = "users")
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "username", nullable = false)
    private String username;

    @NotBlank
    @Size(min = 8, max = 120)
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 100)
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 15)
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = true)
    private String updatedBy;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoteEntity> notes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarEventEntity> events = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        // Reset update fields on creation
        updatedAt = null;
        updatedBy = null;
    }

    public void addNote(NoteEntity note) {
        notes.add(note);
        note.setAuthor(this);
    }

    public void removeNote(NoteEntity note) {
        notes.remove(note);
        note.setAuthor(null);
    }

    public void addEvent(CalendarEventEntity event) {
        events.add(event);
        event.setUser(this);
    }

    public void removeEvent(CalendarEventEntity event) {
        events.remove(event);
        event.setUser(null);
    }
}
