package com.nanum.supplementaryservice.police.vo;

import com.nanum.supplementaryservice.police.domain.Status;
import com.nanum.supplementaryservice.police.domain.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PoliceRequest {
    @NotNull(message = "reportedUserId cannot be null")
    @Schema(description = "신고당한 사람의 ID값을 입력하세요.", defaultValue = "0")
    private Long reportedUserId;
    @NotNull(message = "reporterId cannot be null")
    @Schema(description = "신고자의 ID값을 입력하세요.", defaultValue = "0")
    private Long reporterId;

    @NotNull(message = "type cannot be null")
    @Schema(description = "CHAT, NOTE, BOARD 중 한가지를 입력하세요. ", defaultValue = "NOTE")
    private Type type;

    @NotNull(message = "typeId cannot be null")
    @Schema(description = "Type에 해당되는 ID 값을 입력하세요 ", defaultValue = "0")
    private Long typeId;

    @NotNull(message = "reason cannot be null")
    @Size(min = 1)
    @Schema(description = "신고 사유를 입력하세요. ", defaultValue = "Nope")
    private String reason;

}
