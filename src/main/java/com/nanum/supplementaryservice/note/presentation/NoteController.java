package com.nanum.supplementaryservice.note.presentation;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nanum.config.BaseResponse;
import com.nanum.supplementaryservice.note.application.NoteService;
import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.dto.NoteByUserDto;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.dto.NoteImgDto;
import com.nanum.supplementaryservice.note.dto.NoteListDto;
import com.nanum.supplementaryservice.note.vo.NoteImgResponse;
import com.nanum.supplementaryservice.note.vo.NoteRequest;
import com.nanum.supplementaryservice.note.vo.NoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
@Tag(name = "쪽지", description = "쪽지 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created successfully",
                content = @Content(schema = @Schema(defaultValue = " 등록 신청이 완료되었습니다."))),
        @ApiResponse(responseCode = "400", description = "bad request",
                content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
        @ApiResponse(responseCode = "500", description = "server error",
                content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
})
public class NoteController {
    private final NoteService noteService;
    // GET
    @Operation(summary = "쪽지 등록 API", description = "쪽지를 등록하는 요청")
    @PostMapping(
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> createNote(@Valid @RequestPart NoteRequest noteDetails,
                                             @RequestPart(required = false) List<MultipartFile> images) throws IOException {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        NoteDto noteDto = modelMapper.map(noteDetails, NoteDto.class);
        Long noteId;
        if(images!=null){
            noteId= noteService.createNote(noteDto, images);
        }else{
            noteId =noteService.createNote(noteDto, null);
        }
        HashMap<String,String> result = new HashMap<>();
        String results = "성공!";
        result.put("result", results);
        EntityModel<HashMap<String, String>> entityModel = EntityModel.of(result);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveNote(noteId));
        entityModel.add(linkTo.withRel("detail-note"));
        BaseResponse<EntityModel> baseResponse = new BaseResponse<>(entityModel);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(baseResponse);
//        BaseResponse<String> baseResponse = new BaseResponse<>(result);
        return  ResponseEntity.status(HttpStatus.CREATED).body(jacksonValue);

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
    @GetMapping
    public ResponseEntity<List<Note>> retrieveAllNotes(){
        List<Note> notes = noteService.retrieveNotes();

//        List<NoteResponse> noteResponses = notes.stream()
//                .map(note -> new ModelMapper().map(note, NoteResponse.class))
//                .collect(Collectors.toList());
//
//        BaseResponse<List<NoteResponse>> baseResponse = new BaseResponse<>(noteResponses);
//
//        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
//                .filterOutAllExcept("id", "senderId", "receiverId", "title", "createAt", "readMark");
//        SimpleFilterProvider filters = new SimpleFilterProvider()
//                .addFilter("NoteInfo", filter);
//
//        MappingJacksonValue jacksonValue = new MappingJacksonValue(baseResponse);
//        jacksonValue.setFilters(filters);

        return ResponseEntity.status(HttpStatus.OK).body(notes);

    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Object> retrieveNote(@PathVariable("noteId")Long noteId){
        Note note = noteService.retrieveNoteById(noteId);

        // 1. MAKE VO
        NoteResponse response = new ModelMapper().map(note, NoteResponse.class);
        List<NoteImgResponse> noteImgResponses = Arrays.asList(new ModelMapper().map(note.getNoteImgList(), NoteImgResponse[].class));

        HashMap<String, Object> result = new HashMap<>();
        result.put("note",response);
        result.put("noteImgList",noteImgResponses);
        // 2. MAKE HATEOAS
//        EntityModel<NoteResponse> entityModel = EntityModel.of(response);
//        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllNotes());
//        entityModel.add(linkTo.withRel("all-notes"));

        // 3. MAKE BaseResponse
        BaseResponse<HashMap<String, Object>> baseResponse = new BaseResponse<>(result);

        // 4. MAKE Filter
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "senderId", "receiverId", "title", "createAt","content");
        SimpleFilterProvider filters = new SimpleFilterProvider()
                .addFilter("NoteInfo", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(baseResponse);
        jacksonValue.setFilters(filters);

        return ResponseEntity.status(HttpStatus.OK).body(jacksonValue);
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable("noteId") Long noteId){
        noteService.deleteNote(noteId);
    }
    @DeleteMapping("/{noteId}/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteNoteByUser(@PathVariable("noteId") Long noteId,
                                 @PathVariable("userId") Long userId){
        NoteByUserDto noteByUserDto = new NoteByUserDto();
        noteByUserDto.setNoteId(noteId);
        noteByUserDto.setUserId(userId);

        noteService.deleteNoteByUserId(noteByUserDto);
    }

    @GetMapping("/{userId}/sent")
    public ResponseEntity<BaseResponse<Page<NoteListDto>>> retrieveAllNotesBySender(@PathVariable("userId") Long userId,
                                               @PageableDefault(sort = "createAt",
                                                       direction = Sort.Direction.DESC)
                                               Pageable pageable){

        Page<NoteListDto> noteListDtoList = noteService.retrieveNotesBySent(userId, pageable);
        BaseResponse<Page<NoteListDto>> response = new BaseResponse<>(noteListDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{userId}/received")
    public ResponseEntity<BaseResponse<Page<NoteListDto>>> retrieveAllNotesByReceiver(@PathVariable("userId") Long userId,
                                                                                    @PageableDefault(sort = "createAt",
                                                                                            direction = Sort.Direction.DESC)
                                                                                    Pageable pageable){
        Page<NoteListDto> noteListDtoList = noteService.retrieveNotesByReceived(userId, pageable);
        BaseResponse<Page<NoteListDto>> response = new BaseResponse<>(noteListDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
