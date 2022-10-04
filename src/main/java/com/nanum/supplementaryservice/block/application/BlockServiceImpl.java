package com.nanum.supplementaryservice.block.application;

import com.nanum.config.BaseResponse;
import com.nanum.exception.BlockNotFoundException;
import com.nanum.supplementaryservice.block.domain.Block;
import com.nanum.supplementaryservice.block.dto.BlockDto;
import com.nanum.supplementaryservice.block.dto.BlockedUserDto;
import com.nanum.supplementaryservice.block.infrastructure.BlockRepository;
import com.nanum.supplementaryservice.client.UserServiceClient;
import com.nanum.supplementaryservice.client.vo.UserDto;
import com.nanum.supplementaryservice.client.vo.UserResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService{

    private final BlockRepository blockRepository;
    private final UserServiceClient userServiceClient;

    @Override
    public boolean createBlock(BlockDto blockDto) {
        // check

        UserResponse<UserDto> blockedUser = userServiceClient.getUser(blockDto.getBlockedUserId());
        UserResponse blockerId = userServiceClient.getUser(blockDto.getBlockerId());

        log.info(String.valueOf(blockedUser));
        log.info(String.valueOf(blockerId));

        // lists
//            List<Long> userIds = new ArrayList<>();
//            userIds.add(blockDto.getBlockedUserId());
//            userIds.add(blockDto.getBlockerId());
//            UserResponse usersById = userServiceClient.getUsersById(userIds);
//
//        log.info(String.valueOf(usersById));

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
        UserResponse blockerUser = userServiceClient.getUser(blockerId);

        log.info(String.valueOf(blockerUser));
        List<Long> users = new ArrayList<>();

        // 정렬
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Page<BlockedUserDto>  blockUsers = blockRepository.findByBlockerId(blockerId, pageable).
                map(block -> {
                    users.add(block.getBlockedUserId());
                    return modelMapper.map(block, BlockedUserDto.class);
                        }
                );

        log.info("userInfo:"+String.valueOf(blockUsers.getContent().get(0)));
        if(blockUsers.getContent().size()<1){
            return blockUsers;
        }

        // 유저 id 정리
        log.info("User:"+ String.valueOf(users));
        // 유저 정보 가져오기
        UserResponse<List<UserDto>> usersById = userServiceClient.getUsersById(users);

        return blockUsers.
                map(block -> {
                    for (UserDto user: usersById.getResult()) {
                        if(user.getUserId().equals(block.getBlockedUserId())){
                            block.setUser(user);
                        }
                    }
                 return block;
                });
    }

    @Override
    public boolean validBlockedUserId(BlockDto blockDto) {
    return  blockRepository.existsByBlockerIdAndBlockedUserId(blockDto.getBlockerId(), blockDto.getBlockedUserId());
    }
}
