package com.nanum.supplementaryservice.block.dto;

import com.nanum.supplementaryservice.client.vo.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockedUserDto {
    private Long id;
    private Long blockedUserId;
    private UserDto user;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
