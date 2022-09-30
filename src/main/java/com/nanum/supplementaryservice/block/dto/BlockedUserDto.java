package com.nanum.supplementaryservice.block.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockedUserDto {
    private Long blockedUserId;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
