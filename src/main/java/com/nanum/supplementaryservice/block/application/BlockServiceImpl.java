package com.nanum.supplementaryservice.block.application;

import com.nanum.exception.BlockNotFoundException;
import com.nanum.supplementaryservice.block.domain.Block;
import com.nanum.supplementaryservice.block.dto.BlockDto;
import com.nanum.supplementaryservice.block.dto.BlockedUserDto;
import com.nanum.supplementaryservice.block.infrastructure.BlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService{

    private final BlockRepository blockRepository;

    @Override
    public boolean createBlock(BlockDto blockDto) {
        // check


        boolean result = blockRepository.existsByBlockerIdAndBlockedUserId(blockDto.getBlockerId(), blockDto.getBlockedUserId());

        if(result){
               return false;
        }
        Block block = blockDto.blockDtoToEntity();
        blockRepository.save(block);
        return true;
    }

    @Override
    public void deleteBlock(Long blockId) {
        try {
            blockRepository.deleteById(blockId);
        }catch (Exception ex){
            throw new BlockNotFoundException(String.format("ID[%s] not found",blockId));
        }
    }

    @Override
    public List<Block> retrieveBlocks() {
        return blockRepository.findAll();
    }

    @Override
    public Page<BlockedUserDto> retrieveBlocksByBlocker(Long blockerId, Pageable pageable) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return blockRepository.findByBlockerId(blockerId, pageable).map(block -> modelMapper.map(block, BlockedUserDto.class));
    }

    @Override
    public boolean validBlockedUserId(BlockDto blockDto) {
    return  blockRepository.existsByBlockerIdAndBlockedUserId(blockDto.getBlockerId(), blockDto.getBlockedUserId());
    }
}
