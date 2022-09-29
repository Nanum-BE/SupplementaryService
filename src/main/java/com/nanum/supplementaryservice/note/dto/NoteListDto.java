package com.nanum.supplementaryservice.note.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteListDto {
    private Long id;
    private String title;
    private boolean readMark;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime createAt;
}
