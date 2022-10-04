package com.nanum.kafka.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanum.kafka.dto.Field;
import com.nanum.kafka.dto.KafkaNoteDto;
import com.nanum.kafka.dto.Payload;
import com.nanum.kafka.dto.Schema;
import com.nanum.supplementaryservice.note.dto.NoteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public HashMap<String, Long> sendUserId(String topic, Long userId) {
        HashMap<String,Long> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(userInfo);
        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from the User microservice: " + userInfo);
        return userInfo;
    }

    @Override
    public void createNote(String topic, NoteDto noteDto) {

        List<Field> fields =   Arrays.asList(
            new Field("bigint",true, "sender_id"),
            new Field("varchar(255)",true, "title"),
            new Field("varchar(255)",true, "content"),
            new Field("tinyint(1) default 0",true, "read_mark"),
            new Field("bigint",true, "receiver_id"),
            new Field("bigint default 0",true, "deleter_id")
        );
        Schema schema = Schema.builder()
                .type("struct")
                .fields(fields)
                .optional(false)
                .name("note")
                .build();
        Payload payload = Payload.builder()
                .content(noteDto.getContent())
                .receiverId(noteDto.getReceiverId())
                .senderId(noteDto.getSenderId())
                .title(noteDto.getTitle())
                .build();
        KafkaNoteDto kafkaNoteDto = new KafkaNoteDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaNoteDto);
        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data kafka: " + kafkaNoteDto);
    }
}
