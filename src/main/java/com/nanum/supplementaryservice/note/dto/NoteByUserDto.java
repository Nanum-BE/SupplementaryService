package com.nanum.supplementaryservice.note.dto;

import lombok.Data;

@Data
public class NoteByUserDto {
    private Long userId;
    private Long noteId;
}
