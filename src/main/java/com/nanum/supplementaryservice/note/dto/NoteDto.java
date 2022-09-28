package com.nanum.supplementaryservice.note.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nanum.supplementaryservice.note.domain.Note;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteDto {
    private String title;
    private String content;
    private Long sender;
    private Long receiver;
    private LocalDateTime createAt;

    public Note noteDtoToEntity(){
        return Note.builder()
                .title(getTitle())
                .content(getContent())
                .sender(getSender())
                .receiver(getReceiver())
                .build();
    }
}
