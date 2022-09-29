package com.nanum.supplementaryservice.note.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NoteRequest {
    @NotNull(message = "title cannot be null")
    @Size(min = 1)
    @Schema(description = "제목을 입력하세요.", defaultValue = "title")
    private String title;

    @NotNull(message = "content cannot be null")
    @Schema(description = "내용을 입력하세요.", defaultValue = "content")
    @Size(min = 1)
    private String content;

    @NotNull(message = "receiver cannot be null")
    @Schema(description = "수신자의 ID값을 입력하세요.", defaultValue = "0")
    private Long receiverId;
}
