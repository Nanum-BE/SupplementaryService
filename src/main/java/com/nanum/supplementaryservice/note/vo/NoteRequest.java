package com.nanum.supplementaryservice.note.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NoteRequest {
    @NotNull(message = "title cannot be null")
    @Size(min = 1)
    private String title;

    @NotNull(message = "content cannot be null")
    @Size(min = 1)
    private String content;

    @NotNull(message = "receiver cannot be null")
    @Size(min = 1)
    private String receiver;
}
