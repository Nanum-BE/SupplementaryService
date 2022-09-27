package com.nanum.supplementaryservice.note.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private boolean read;
    private Long sender;
    private Long receiver;
    private LocalDateTime createAt;
}
