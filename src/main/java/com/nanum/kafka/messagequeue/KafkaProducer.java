package com.nanum.kafka.messagequeue;

import com.nanum.supplementaryservice.note.dto.NoteDto;

import java.util.HashMap;


public interface KafkaProducer {

    HashMap<String, Long> sendUserId(String topic, Long userId);

    void createNote(String topic, NoteDto noteDto);
}
