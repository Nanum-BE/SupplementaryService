package com.nanum.supplementaryservice.note.application;

import com.nanum.exception.ImgNotFoundException;
import com.nanum.exception.NoteNotFoundException;
import com.nanum.supplementaryservice.client.UserServiceClient;
import com.nanum.supplementaryservice.client.vo.UserDto;
import com.nanum.supplementaryservice.client.vo.UserResponse;
import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.domain.NoteImg;
import com.nanum.supplementaryservice.note.dto.*;
import com.nanum.supplementaryservice.note.infrastructure.NoteImgRepository;
import com.nanum.supplementaryservice.note.infrastructure.NoteRepository;
import com.nanum.supplementaryservice.note.vo.NoteImgResponse;
import com.nanum.supplementaryservice.note.vo.NoteResponse;
import com.nanum.utils.s3.application.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;
    private final S3Service s3Service;
    private final NoteImgRepository noteImgRepository;
//    private final KafkaProducer kafkaProducer;
    private final UserServiceClient userServiceClient;

    @Override
    public Long createNote(NoteDto noteDto, List<MultipartFile> images)  {
        /* user*/
        UserResponse blockedUser = userServiceClient.getUser(noteDto.getReceiverId());
        UserResponse blockerId = userServiceClient.getUser(noteDto.getSenderId());

        Note note = noteDto.noteDtoToEntity();
        if(images!=null){
            // s3 변환
            for (MultipartFile image: images) {

                try {
                    HashMap<String, String> uploadHash = s3Service.uploadHash(image);
                    /*
                        kafka if...

                     */
//                    kafkaProducer.createNote("note",noteDto);
                    NoteImgDto noteImgDto = NoteImgDto.builder()
                            .imgPath(uploadHash.get("imgPath"))
                            .originName(uploadHash.get("originName"))
                            .savedName(uploadHash.get("savedName"))
                            .build();
                    NoteImg noteImg = noteImgDto.noteImgDtoToEntity();
                    note.addNoteImg(noteImg);
                }catch (IOException ex){
                    throw new ImgNotFoundException(String.format("Image [%s] not found",image));
                }
            }
        }
        return noteRepository.save(note).getId();

    }

    @Override
    public Page<NoteListDto> retrieveNotesBySent(Long userId, Pageable pageable) {
        userServiceClient.getUser(userId);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        log.info(String.valueOf(userId));

        List<Long> users = new ArrayList<>();

        // 유효성 검사
        Page<NoteListDto> noteListDtos = noteRepository.findBySenderIdAndDeleterIdIsNot(userId, userId, pageable).map(note ->
        {
            users.add(note.getReceiverId());
            return modelMapper.map(note, NoteListDto.class);
        });
        if(noteListDtos.getContent().size()<1){
            return noteListDtos;
        }

        UserResponse<List<UserDto>> usersById = userServiceClient.getUsersById(users);

        // 리스트 가져오기
        return noteListDtos.
                map(noteListDto -> {
                    for (UserDto user: usersById.getResult()) {
                        if(user.getUserId().equals(noteListDto.getReceiverId())){
                            noteListDto.setReceiver(user);
                        }
                    }
                    return noteListDto;
                });
    }

    @Override
    public  Page<NoteListDto> retrieveNotesByReceived(Long userId,Pageable pageable) {
        userServiceClient.getUser(userId);
        List<Long> users = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Page<NoteListDto> noteListDtos = noteRepository.findByReceiverIdAndDeleterIdIsNot(userId, userId,pageable).map(note ->
        {
            users.add(note.getSenderId());
            return modelMapper.map(note, NoteListDto.class);
        });


        if(noteListDtos.getContent().size()<1){
            return noteListDtos;
        }
        UserResponse<List<UserDto>> usersById = userServiceClient.getUsersById(users);

        return noteListDtos.
                map(noteListDto -> {
                    for (UserDto user: usersById.getResult()) {
                        if(user.getUserId().equals(noteListDto.getSenderId())){
                            noteListDto.setSender(user);
                        }
                    }
                    return noteListDto;
                });
    }

    @Override
    public List<Note> retrieveNotes() {
        return noteRepository.findAll();
    }


    @Override
    public void deleteNoteBySenderId(NoteByUserDto noteByUserDto) {

    }

    @Override
    public void deleteNoteByUserId(NoteByUserDto noteByUserDto) {
        // user 검색
        userServiceClient.getUser(noteByUserDto.getUserId());

        Optional<Note> note = noteRepository.findById(noteByUserDto.getNoteId());
        if(note.isEmpty()){
            throw new NoteNotFoundException(String.format("Note [%s] not found",noteByUserDto.getNoteId()));
        }
        ModelMapper modelMapper = new ModelMapper();
        NoteChangeDto noteChangeDto = modelMapper.map(note.get(), NoteChangeDto.class);
        Long deleterId = noteChangeDto.getDeleterId();
        // 2명 이상이 삭제할 때
        if(deleterId != 0){
            deleteNote(noteByUserDto.getNoteId());
            return;
        }

        noteChangeDto.setDeleterId(noteByUserDto.getUserId());
        Note changeNote = noteChangeDto.NoteChangeDto();
        noteRepository.save(changeNote);

    }

    @Override
    public void deleteNote(Long noteId) {
        try{
            noteRepository.deleteById(noteId);
            noteImgRepository.deleteAllByNoteId(noteId);
        } catch (Exception ex){
            throw new NoteNotFoundException(String.format("ID[%s] not found",noteId));
        }
    }

    @Override
    public HashMap<String, Object> retrieveNoteById(Long noteId) {
        Optional<Note> note = noteRepository.findById(noteId);
        if(note.isEmpty()){
            throw new NoteNotFoundException(String.format("ID[%s] not found",noteId));
        }
        Note sendNote = note.get();

        if(!note.get().isReadMark()){
            // 읽음처리하기
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            NoteChangeDto noteChangeDto = modelMapper.map(note.get(), NoteChangeDto.class);
            noteChangeDto.setReadMark(true);
            Note changeNote = noteChangeDto.NoteChangeDto();
            sendNote = noteRepository.save(changeNote);
        }

        NoteResponse response = new ModelMapper().map(sendNote, NoteResponse.class);
        UserResponse<UserDto> receiver = userServiceClient.getUser(sendNote.getReceiverId());
        UserResponse<UserDto> sender = userServiceClient.getUser(sendNote.getSenderId());
        response.setReceiver(receiver.getResult());
        response.setSender(sender.getResult());


        List<NoteImgResponse> noteImgResponses = Arrays.asList(new ModelMapper().map(sendNote.getNoteImgList(), NoteImgResponse[].class));

        HashMap<String, Object> result = new HashMap<>();
        result.put("note",response);
        result.put("noteImgList",noteImgResponses);

        return result;

    }

    @Override
    public Boolean existsById(Long noteId) {
        boolean existsById = noteRepository.existsById(noteId);
        if(!existsById){
            throw new NoteNotFoundException(String.format("ID[%s] not found",noteId));
        }

        return true;
    }
}
