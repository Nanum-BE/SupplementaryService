package com.nanum.supplementaryservice.note.vo;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.supplementaryservice.client.vo.UserDto;
import com.nanum.supplementaryservice.note.domain.NoteImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(value = {"title"})  - 해당 값 빼고 출력
@JsonFilter("NoteInfo")
public class NoteResponse {

    private Long id;
    private String title;
    private String content;
    private boolean readMark;
    private Long senderId;
    private Long receiverId;
    private UserDto receiver;
    private UserDto sender;
    private LocalDateTime createAt;

}
