package com.nanum.supplementaryservice.block.dto;

import com.nanum.supplementaryservice.block.domain.Block;
import lombok.Data;

@Data
public class BlockDto {
    private Long blockerId;
    private Long blockedUserId;

    public Block blockDtoToEntity(){
        return Block.builder()
                .blockedUserId(getBlockedUserId())
                .blockerId(getBlockerId())
                .build();
    }
}
