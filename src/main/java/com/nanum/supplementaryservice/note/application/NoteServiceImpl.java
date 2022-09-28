package com.nanum.supplementaryservice.note.application;

import com.nanum.exception.ImgNotFoundException;
import com.nanum.exception.NoteNotFoundException;
import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.domain.NoteImg;
import com.nanum.supplementaryservice.note.dto.NoteByUserDto;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import com.nanum.supplementaryservice.note.dto.NoteImgDto;
import com.nanum.supplementaryservice.note.infrastructure.NoteRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;
    private final S3Service s3Service;
    @Override
    public void createNote(NoteDto noteDto, List<MultipartFile> images)  {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Note note = noteDto.noteDtoToEntity();
        if(images!=null){
            // s3 변환
            for (MultipartFile image: images) {

                try {
                    HashMap<String, String> uploadHash = s3Service.uploadHash(image);
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

        noteRepository.save(note);

    }

    @Override
    public Page<Note> retrieveNotesBySent(Long userId, Pageable pageable) {
        // 유효성 검사
        Page<Note> notes = noteRepository.findBySender(userId, pageable);
        //
        return notes;
    }

    @Override
    public List<Note> retrieveNotesByReceived(Long UserId) {
        return null;
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
