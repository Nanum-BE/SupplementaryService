package com.nanum.supplementaryservice.police.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.supplementaryservice.block.domain.Block;
import com.nanum.supplementaryservice.block.dto.BlockDto;
import com.nanum.supplementaryservice.block.dto.BlockedUserDto;
import com.nanum.supplementaryservice.block.vo.BlockRequest;
import com.nanum.supplementaryservice.police.application.PoliceService;
import com.nanum.supplementaryservice.police.dto.PoliceDto;
import com.nanum.supplementaryservice.police.vo.PoliceChangeRequest;
import com.nanum.supplementaryservice.police.vo.PoliceRequest;
import com.nanum.supplementaryservice.police.vo.PoliceResponse;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/polices")
@Tag(name = "신고", description = "신고 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created successfully",
                content = @Content(schema = @Schema(defaultValue = " 등록 신청이 완료되었습니다."))),
        @ApiResponse(responseCode = "400", description = "bad request",
                content = @Content(schema = @Schema(defaultValue = "잘못된 입력 값입니다."))),
        @ApiResponse(responseCode = "500", description = "server error",
                content = @Content(schema = @Schema(defaultValue = "서버 에러입니다."))),
})
public class PoliceController {
    private final PoliceService policeService;

    @Operation(summary = "신고 등록 API", description = "신고 등록하는 요청")
    @PostMapping
    public ResponseEntity<Object> createBlock(@Valid @RequestBody PoliceRequest policeRequest) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PoliceDto policeDto = modelMapper.map(policeRequest, PoliceDto.class);
        boolean police = policeService.createPolice(policeDto);
        if(!police){
            BaseResponse<Object> baseResponse = new BaseResponse<>("기존에 신고한 사람입니다.", "No");
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(baseResponse);
        }

        HashMap<String, Boolean> result = new HashMap<>();
        result.put("policeId", true);
        BaseResponse<Object> baseResponse = new BaseResponse<>(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<PoliceResponse>>> retrievePolices(@PageableDefault(sort = "createAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PoliceResponse> baseResponses = policeService.retrievePolices(pageable);
        BaseResponse<Page<PoliceResponse>> baseResponse = new BaseResponse<>(baseResponses);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @PutMapping
    public ResponseEntity<Object> acceptedPolice(@RequestBody PoliceChangeRequest policeChangeRequest) {

         policeService.acceptedPolice(policeChangeRequest.getPoliceId());

        HashMap<String, String> result = new HashMap<>();
        result.put("result", "값이 변경되었습니다.");
        BaseResponse<Object> baseResponse = new BaseResponse<>(result);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
    @DeleteMapping("{policeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePolice(@PathVariable("policeId") Long policeId){
        policeService.deletePolice(policeId);
    }

}
