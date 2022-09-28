package com.nanum.supplementaryservice.note.application;

import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.dto.NoteByUserDto;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.vo.NoteResponse;

import java.util.List;

public interface NoteService{

    /*
        1. 메일 보내기
        2. 보낸 메일 출력(pageable)
        3. 받은 메일 출력
        4. 상세 보기
        5. 삭제 하기
     */
    void createNote(NoteDto noteDto);
    List<Note> retrieveNotes();
    void deleteNoteBySenderId(NoteByUserDto noteByUserDto);
    void deleteNote(Long noteId);

    Note retrieveNoteById(Long noteId);
}
