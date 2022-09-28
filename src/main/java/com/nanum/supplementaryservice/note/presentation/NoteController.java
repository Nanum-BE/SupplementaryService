package com.nanum.supplementaryservice.note.presentation;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nanum.config.BaseResponse;
import com.nanum.supplementaryservice.note.application.NoteService;
import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.vo.NoteRequest;
import com.nanum.supplementaryservice.note.vo.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NoteController {
    private final NoteService noteService;
    // GET
    @PostMapping("/notes/{userId}")
    public ResponseEntity<Object> createNote(@Valid @RequestBody NoteRequest noteRequest, @PathVariable("userId") Long userId){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        NoteDto noteDto = modelMapper.map(noteRequest, NoteDto.class);
        noteDto.setSender(userId);

        noteService.createNote(noteDto);

        String result = "성공!";

        BaseResponse<String> response = new BaseResponse<>(result);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    /**
     * 버전 관리
     * 1. URI Versioning
     *  - Twitter
     *  @GetMapping("/v1/notes")
     * 2. Request Parameter versioning
     *  - Amazon
     *  users/1?version=1*
     *  @GetMapping(value = "/users/{id}", params = "version=1")
     *
     *  3. Media type versioning (a.k.a "content negotiation" or "accept header")
     *  - Github
     *   X-API-VERSION = 1 헤더값에 입력하기
     *   @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")
     *   *   *   *  *  *  *
     *  4. (Custom) headers versioning
     *   - Microsoft
     *  header에 키값으로 Accept , Value 값: application/vnd.company.appv1+json
     *  @GetMapping(value = "/notes",produces = "application/vnd.company.appv1+json")*  * *
     *
     */
    @GetMapping("/notes")
    public ResponseEntity<Object> retrieveAllNotes(){
        List<Note> notes = noteService.retrieveNotes();

        List<NoteResponse> noteResponses = notes.stream()
                .map(note -> new ModelMapper().map(note, NoteResponse.class))
                .collect(Collectors.toList());

        BaseResponse<List<NoteResponse>> baseResponse = new BaseResponse<>(noteResponses);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "sender", "receiver", "title", "createAt", "read");
        SimpleFilterProvider filters = new SimpleFilterProvider()
                .addFilter("NoteInfo", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(baseResponse);
        jacksonValue.setFilters(filters);

        return ResponseEntity.status(HttpStatus.OK).body(jacksonValue);

    }

    @GetMapping("/notes/{noteId}")
    public ResponseEntity<Object> retrieveNote(@PathVariable("noteId")Long noteId){
        Note note = noteService.retrieveNoteById(noteId);

        // 1. MAKE VO
        NoteResponse response = new ModelMapper().map(note, NoteResponse.class);

        // 2. MAKE HATEOAS
        EntityModel<NoteResponse> entityModel = EntityModel.of(response);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllNotes());
        entityModel.add(linkTo.withRel("all-notes"));


        // 3. MAKE BaseResponse
        BaseResponse<EntityModel> baseResponse = new BaseResponse<>(entityModel);


        // 4. MAKE Filter
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "sender", "receiver", "title", "createAt","content");
        SimpleFilterProvider filters = new SimpleFilterProvider()
                .addFilter("NoteInfo", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(baseResponse);
        jacksonValue.setFilters(filters);

        return ResponseEntity.status(HttpStatus.OK).body(jacksonValue);
    }

    @DeleteMapping("/notes/{noteId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long noteId){
        noteService.deleteNote(noteId);
    }



}
