package com.nanum.supplementaryservice.note.infrastructure;

import com.nanum.supplementaryservice.note.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository  extends JpaRepository<Note, Long> {
    Page<Note> findBySender(Long sender, Pageable pageable);
}

