package com.nanum.supplementaryservice.note.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.domain.NoteImg;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteDto {
    private String title;
    private String content;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime createAt;
//
//    private List<MultipartFile> noteImgList;
    public Note noteDtoToEntity(List<NoteImg> noteImgList){
        return Note.builder()
                .title(getTitle())
                .content(getContent())
                .senderId(getSenderId())
                .receiverId(getReceiverId())
                .noteImgList(noteImgList)
                .build();
    }
    public Note noteDtoToEntity(){
        return Note.builder()
                .title(getTitle())
                .content(getContent())
                .senderId(getSenderId())
                .receiverId(getReceiverId())
                .build();
    }
}
