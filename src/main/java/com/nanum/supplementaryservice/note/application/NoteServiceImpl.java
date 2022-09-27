package com.nanum.supplementaryservice.note.application;

import com.nanum.exception.NoteNotFoundException;
import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.dto.NoteByUserDto;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.infrastructure.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;

    @Override
    public void createNote(NoteDto noteDto) {
        Note note = noteDto.noteDtoToEntity();
        noteRepository.save(note);
    }

    @Override
    public List<Note> retrieveNotes() {
        List<Note> notes = noteRepository.findAll();

        return notes;
    }


    @Override
    public void deleteNoteBySenderId(NoteByUserDto noteByUserDto) {

    }

    @Override
    public void deleteNote(Long noteId) {
        try{
            noteRepository.deleteById(noteId);
        } catch (Exception ex){
            throw new NoteNotFoundException(String.format("ID[%s] not found",noteId));
        }
    }

    @Override
    public Note retrieveNoteById(Long noteId) {
        Optional<Note> note = noteRepository.findById(noteId);
        if(note.isEmpty()){
            throw new NoteNotFoundException(String.format("ID[%s] not found",noteId));
        }
        return note.get();
    }
}
