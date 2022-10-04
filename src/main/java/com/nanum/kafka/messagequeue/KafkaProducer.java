package com.nanum.kafka.messagequeue;

import java.util.HashMap;

public interface KafkaProducer {
    HashMap<String, Long> sendUserId(String topic, Long userId);
}
