package com.nanum.supplementaryservice.note.vo;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(value = {"title"})  - 해당 값 빼고 출력
@JsonFilter("NoteInfo")
public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private boolean read;
    private Long sender;
    private Long receiver;
    private LocalDateTime createAt;
}
