package com.nanum.supplementaryservice.note.presentation;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nanum.config.BaseResponse;
import com.nanum.supplementaryservice.note.application.NoteService;
import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.vo.NoteRequest;
import com.nanum.supplementaryservice.note.vo.NoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "쪽지", description = "쪽지 관련 api")
public class NoteController {
    private final NoteService noteService;
    // GET
    @Operation(summary = "쪽지 등록 API", description = "쪽지를 등록하는 요청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created successfully", content = @Content(schema = @Schema(defaultValue = "하우스 등록 신청이 완료되었습니다."))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
            @ApiResponse(responseCode = "500", description = "server error", content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
    })
    @PostMapping("/notes/{senderId}")
    public ResponseEntity<Object> createNote(@PathVariable("senderId") Long senderId,
                                             @Valid @RequestPart NoteRequest noteRequest,
                                             @RequestPart(required = false) List<MultipartFile> images){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        NoteDto noteDto = modelMapper.map(noteRequest, NoteDto.class);
        noteDto.setSender(senderId);

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
