package com.nanum.supplementaryservice.block.application;

import com.nanum.supplementaryservice.block.domain.Block;
import com.nanum.supplementaryservice.block.dto.BlockDto;
import com.nanum.supplementaryservice.block.dto.BlockedUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlockService {
    /*
    1. 차단 생성
    2. 차단 취소
    3. 차단 목록
    4. 차단한 사람의 차단 당한 사람들의 목록
    5. 차단 여부 확인
     */
    boolean createBlock(BlockDto blockDto);

    void deleteBlock(Long blockId);

    List<Block> retrieveBlocks();

    Page<BlockedUserDto> retrieveBlocksByBlocker(Long BlockerId, Pageable pageable);

    boolean validBlockedUserId(BlockDto blockDto);

}
