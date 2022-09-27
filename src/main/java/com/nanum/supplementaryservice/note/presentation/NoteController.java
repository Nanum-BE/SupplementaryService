package com.nanum.supplementaryservice.note.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.supplementaryservice.note.application.NoteService;
import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.vo.NoteRequest;
import com.nanum.supplementaryservice.note.vo.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NoteController {
    private final NoteService noteService;
    // GET
    @PostMapping("/notes/{userId}")
    public ResponseEntity<Object> createNote(@RequestBody NoteRequest noteRequest, @PathVariable("userId") Long userId){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        NoteDto noteDto = modelMapper.map(noteRequest, NoteDto.class);
        noteDto.setSender(userId);

        noteService.createNote(noteDto);

        String result = "성공!";

        BaseResponse<String> response = new BaseResponse<>(result);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/notes")
    public ResponseEntity<Object> retrieveNotes(){
        List<Note> notes = noteService.retrieveNotes();
        List<NoteResponse> noteResponses = notes.stream()
                .map(note -> new ModelMapper().map(note, NoteResponse.class))
                .collect(Collectors.toList());
        BaseResponse<List<NoteResponse>> baseResponse = new BaseResponse<>(noteResponses);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping("/notes/{noteId}")
    public ResponseEntity<Object> retrieveNotes(@PathVariable("noteId")Long noteId){
        Note note = noteService.retrieveNoteById(noteId);
        NoteResponse response = new ModelMapper().map(note, NoteResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/notes/{noteId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long noteId){
        noteService.deleteNote(noteId);
    }



}
