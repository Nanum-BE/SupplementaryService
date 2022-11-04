package com.nanum.supplementaryservice.note.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.supplementaryservice.client.vo.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteListDto {
    private Long id;
    private String title;
    private boolean readMark;
    private Long senderId;
    private UserDto sender;
    private Long receiverId;
    private UserDto receiver;
    private LocalDateTime createAt;
    private String content;
}
