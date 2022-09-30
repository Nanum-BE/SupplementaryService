package com.nanum.supplementaryservice.block.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BlockRequest {


    @NotNull(message = "blocked_userId cannot be null")
    @Schema(description = "차단당한사람의 ID값을 입력하세요.", defaultValue = "0")
    private Long blockedUserId;
}
