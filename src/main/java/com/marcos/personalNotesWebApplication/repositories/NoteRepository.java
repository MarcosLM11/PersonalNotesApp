package com.marcos.personalNotesWebApplication.repositories;

import com.marcos.personalNotesWebApplication.entities.NoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {

    /**
     * Finds all notes created by a specific user with pagination.
     * Using Page when you need total count and complete pagination info.
     *
     * @param userId the ID of the user who created the notes
     * @param pageable pagination and sorting information
     * @return a page of notes created by the specified user
     */
    Page<NoteEntity> findByAuthorId(UUID userId, Pageable pageable);

    /**
     * Finds all notes created by a specific user with pagination (no total count).
     * Using Slice for better performance when you don't need total count.
     *
     * @param userId the ID of the user who created the notes
     * @param pageable pagination and sorting information
     * @return a slice of notes created by the specified user
     */
    Slice<NoteEntity> findByAuthorIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    /**
     * Finds public notes with pagination.
     *
     * @param pageable pagination and sorting information
     * @return a page of public notes
     */
    Page<NoteEntity> findByIsPublicTrue(Pageable pageable);

    /**
     * Search notes by title or content with pagination.
     * Using ILIKE for case-insensitive search (PostgreSQL).
     *
     * @param searchTerm the search term
     * @param pageable pagination and sorting information
     * @return a page of matching notes
     */
    @Query("SELECT n FROM notes n WHERE " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<NoteEntity> searchNotes(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Finds notes by tags with pagination.
     *
     * @param tag the tag to search for
     * @param pageable pagination and sorting information
     * @return a page of notes containing the tag
     */
    @Query("SELECT n FROM notes n WHERE LOWER(n.tags) LIKE LOWER(CONCAT('%', :tag, '%'))")
    Page<NoteEntity> findByTagsContainingIgnoreCase(@Param("tag") String tag, Pageable pageable);

    /**
     * Legacy method - kept for backward compatibility.
     * Consider using paginated versions above.
     *
     * @param userId the ID of the user who created the notes
     * @return a collection of notes created by the specified user
     */
    @Deprecated
    List<NoteEntity> findByAuthorId(UUID userId);
}
