package com.marcos.personalNotesWebApplication.repositories;

import com.marcos.personalNotesWebApplication.entities.NoteEntity;
import com.marcos.personalNotesWebApplication.entities.NoteVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface NoteVersionRepository extends JpaRepository<NoteVersionEntity, UUID> {

    List<NoteVersionEntity> findByNote(NoteEntity noteEntity);
}
