package com.nanum.kafka.dto;

import com.nanum.config.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload extends BaseTimeEntity {
    private String title;
    private String content;
    private boolean readMark;
    private Long senderId;
    private Long receiverId;
    private Long deleterId;
}
