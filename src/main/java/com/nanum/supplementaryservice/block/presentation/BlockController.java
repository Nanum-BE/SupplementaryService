package com.nanum.supplementaryservice.block.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.supplementaryservice.block.application.BlockService;
import com.nanum.supplementaryservice.block.domain.Block;
import com.nanum.supplementaryservice.block.dto.BlockDto;
import com.nanum.supplementaryservice.block.dto.BlockedUserDto;
import com.nanum.supplementaryservice.block.vo.BlockRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blocks")
@Tag(name = "차단", description = "차단 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created successfully",
                content = @Content(schema = @Schema(defaultValue = " 등록 신청이 완료되었습니다."))),
        @ApiResponse(responseCode = "400", description = "bad request",
                content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
        @ApiResponse(responseCode = "500", description = "server error",
                content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
})
public class BlockController {
    private final BlockService blockService;

    @Operation(summary = "차단 등록 API", description = "차단 등록하는 요청")
    @PostMapping("/{blockerId}")
    public ResponseEntity<Object> createBlock(@PathVariable("blockerId") Long blockerId,
                                              @Valid @RequestBody BlockRequest blockRequest) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        BlockDto blockDto = modelMapper.map(blockRequest, BlockDto.class);
        blockDto.setBlockerId(blockerId);
        Long block = blockService.createBlock(blockDto);
        BaseResponse<Long> baseResponse = new BaseResponse<>(block);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @GetMapping
    public ResponseEntity<List<Block>> retrieveAllBlocks() {
        List<Block> blocks = blockService.retrieveBlocks();
        return ResponseEntity.status(HttpStatus.OK).body(blocks);
    }

    @GetMapping("/{blockerId}")
    public ResponseEntity<BaseResponse<Page<BlockedUserDto>>> retrieveAllBlocksByBlocker(@PathVariable("blockerId") Long blockerId,
                                                                                         @PageableDefault(sort = "createAt",
                                                                                                 direction = Sort.Direction.DESC)
                                                                                         Pageable pageable) {
        Page<BlockedUserDto> blockedUserDtos = blockService.retrieveBlocksByBlocker(blockerId, pageable);
        BaseResponse<Page<BlockedUserDto>> baseResponse = new BaseResponse<>(blockedUserDtos);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping("/blocker/{blockerId}/blocked/{blockedUserId}")
    public ResponseEntity<BaseResponse<Boolean>> validBlock(@PathVariable("blockerId") Long blockerId,
                                                           @PathVariable("blockedUserId") Long blockedUserId){
        BlockDto blockDto = new BlockDto();
        blockDto.setBlockedUserId(blockedUserId);
        blockDto.setBlockerId(blockerId);
        boolean validBlock = blockService.validBlockedUserId(blockDto);
        BaseResponse<Boolean> baseResponse = new BaseResponse<>(validBlock);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

    }


}
