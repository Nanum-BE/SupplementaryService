package com.nanum.supplementaryservice.note.application;

import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.dto.NoteByUserDto;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.dto.NoteListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface NoteService{

    /*
        1. 메일 보내기
        2. 보낸 메일 출력(pageable)
        3. 받은 메일 출력
        4. 상세 보기
        5. 삭제 하기
     */
    Long createNote(NoteDto noteDto, List<MultipartFile> images) throws IOException;

    Page<NoteListDto> retrieveNotesBySent(Long UserId, Pageable pageable);
    Page<NoteListDto> retrieveNotesByReceived(Long userId,Pageable pageable);
    List<Note> retrieveNotes();
    void deleteNote(Long noteId);
    HashMap<String, Object> retrieveNoteById(Long noteId);
    Boolean existsById(Long noteId);

    void deleteNoteBySenderId(NoteByUserDto noteByUserDto);

    void deleteNoteByUserId(NoteByUserDto noteByUserDto);

    int countNotesByReceived(Long userId);

    HashMap<String, Object> retrieveNoteByIdAndUserId(NoteByUserDto noteByUserDto);
}
