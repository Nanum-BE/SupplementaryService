package com.nanum.supplementaryservice.note.dto;

import com.nanum.supplementaryservice.note.domain.Note;
import com.nanum.supplementaryservice.note.domain.NoteImg;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteChangeDto {
    private Long id;
    private String title;
    private String content;
    private boolean readMark;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime senderDeleteAt;
    private LocalDateTime ReceiverDeleteAt;
    private List<NoteImg> noteImgList;
    public Note NoteChangeDto(){
        return Note.builder()
                .noteImgList(getNoteImgList())
                .title(getTitle())
                .content(getContent())
                .senderId(getSenderId())
                .receiverId(getReceiverId())
                .id(getId())
                .readMark(isReadMark())
                .ReceiverDeleteAt(getReceiverDeleteAt())
                .senderDeleteAt(getSenderDeleteAt())
                .build();
    }
}
