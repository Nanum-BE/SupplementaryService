package com.nanum.supplementaryservice.note.application;

import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.dto.NoteByUserDto;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.vo.NoteResponse;

import java.util.List;

public interface NoteService{
    void createNote(NoteDto noteDto);
    List<Note> retrieveNotes();
    void deleteNoteBySenderId(NoteByUserDto noteByUserDto);
    void deleteNote(Long noteId);

    Note retrieveNoteById(Long noteId);
}
