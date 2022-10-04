package com.nanum.supplementaryservice.police.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanum.supplementaryservice.client.vo.UserDto;
import com.nanum.supplementaryservice.police.domain.Status;
import com.nanum.supplementaryservice.police.domain.Type;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private UserDto reportedUser;
    private UserDto reporter;
}
