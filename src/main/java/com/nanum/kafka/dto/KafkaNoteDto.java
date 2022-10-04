package com.nanum.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaNoteDto {
    private Schema schema;
    private Payload payload;
}
