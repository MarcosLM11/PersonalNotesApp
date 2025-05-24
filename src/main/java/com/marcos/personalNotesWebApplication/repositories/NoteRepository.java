package com.marcos.personalNotesWebApplication.repositories;

import com.marcos.personalNotesWebApplication.entities.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity,UUID> {

    /**
     * Finds all notes created by a specific user.
     *
     * @param userId the ID of the user who created the notes
     * @return a collection of notes created by the specified user
     */
    List<NoteEntity> findByAuthorId(UUID userId);
}
