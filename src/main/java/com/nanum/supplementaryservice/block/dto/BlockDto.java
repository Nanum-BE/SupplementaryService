package com.nanum.supplementaryservice.block.dto;

import com.nanum.supplementaryservice.block.domain.Block;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockDto {
    private Long blockerId;
    private Long blockedUserId;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    public Block blockDtoToEntity(){
        return Block.builder()
                .blockedUserId(getBlockedUserId())
                .blockerId(getBlockerId())
                .build();
    }
}
