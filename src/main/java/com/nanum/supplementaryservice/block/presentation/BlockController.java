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
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blocks")
@Tag(name = "차단", description = "차단 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created successfully",
                content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "bad request",
                content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
        @ApiResponse(responseCode = "500", description = "server error",
                content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
})
public class BlockController {
    private final BlockService blockService;

    @Operation(summary = "차단 등록 API", description = "차단 등록하는 요청")
    @PostMapping
    public ResponseEntity<Object> createBlock(@Valid @RequestBody BlockRequest blockRequest) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        BlockDto blockDto = modelMapper.map(blockRequest, BlockDto.class);
        boolean block = blockService.createBlock(blockDto);
        if(!block){
            BaseResponse<Object> baseResponse = new BaseResponse<>("기존에 차단한 사람입니다.", "No");
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(baseResponse);
        }

        HashMap<String, Boolean> result = new HashMap<>();
        result.put("blockId", true);
        BaseResponse<Object> baseResponse = new BaseResponse<>(result);
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
    public ResponseEntity<BaseResponse<HashMap<String, Boolean>>>validBlock(@PathVariable("blockerId") Long blockerId,
                                                           @PathVariable("blockedUserId") Long blockedUserId){
        BlockDto blockDto = new BlockDto();
        blockDto.setBlockedUserId(blockedUserId);
        blockDto.setBlockerId(blockerId);
        boolean validBlock = blockService.validBlockedUserId(blockDto);
        HashMap<String,Boolean> result = new HashMap<>();
        result.put("valid",validBlock);
        BaseResponse<HashMap<String, Boolean>> baseResponse = new BaseResponse<>(result);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

    }
    @DeleteMapping("/{blockId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBlock(@PathVariable("blockId") Long blockId){
        blockService.deleteBlock(blockId);
    }

}
