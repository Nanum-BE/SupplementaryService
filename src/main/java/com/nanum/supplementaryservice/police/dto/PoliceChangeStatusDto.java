package com.nanum.supplementaryservice.police.dto;

import com.nanum.supplementaryservice.police.domain.Police;
import com.nanum.supplementaryservice.police.domain.Status;
import com.nanum.supplementaryservice.police.domain.Type;
import lombok.Data;

@Data
public class PoliceChangeStatusDto {
    private Long id;
    private Long reportedUserId;
    private Long reporterId;
    private Type type;
    private Long typeId;
    private Status status;
    private String reason;
    public Police policeDtoTOEntity(){
        return Police.builder()
                .id(getId())
                .reportedUserId(getReportedUserId())
                .type(getType())
                .typeId(getTypeId())
                .reason(getReason())
                .status(getStatus())
                .reporterId(getReporterId())
                .build();
    }
}
