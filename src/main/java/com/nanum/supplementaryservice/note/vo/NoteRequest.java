package com.nanum.supplementaryservice.note.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NoteRequest {
    @NotNull(message = "title cannot be null")
    private String title;

    @NotNull(message = "content cannot be null")
    private String content;

    @NotNull(message = "receiver cannot be null")
    private String receiver;
}
