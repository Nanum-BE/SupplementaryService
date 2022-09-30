package com.nanum.supplementaryservice.police.dto;

import com.nanum.supplementaryservice.police.domain.Police;
import com.nanum.supplementaryservice.police.domain.Status;
import com.nanum.supplementaryservice.police.domain.Type;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class PoliceDto {

    private Long reportedUserId;
    private Long reporterId;
    private Type type;
    private Long typeId;
    private String reason;

    public Police policeDtoTOEntity(){
        return Police.builder()
                .reportedUserId(getReportedUserId())
                .type(getType())
                .typeId(getTypeId())
                .reason(getReason())
                .reporterId(getReporterId())
                .build();
    }
}
