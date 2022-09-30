package com.nanum.supplementaryservice.police.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PoliceChangeRequest {
    @NotNull(message = "policeId cannot be null")
    @Schema(description = "police ID값을 입력하세요.", defaultValue = "0")
    private Long policeId;

}
