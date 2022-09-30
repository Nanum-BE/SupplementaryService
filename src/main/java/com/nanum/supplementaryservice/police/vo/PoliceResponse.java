package com.nanum.supplementaryservice.police.vo;

import com.nanum.supplementaryservice.police.domain.Status;
import com.nanum.supplementaryservice.police.domain.Type;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PoliceResponse {
    private Long id;
    private Long reportedUserId;
    private Long reporterId;
    private Type type;
    private Long typeId;
    private Status status;
    private String reason;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
