package com.nanum.supplementaryservice.note.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nanum.config.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Note extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;


    @Column(columnDefinition = "boolean default false")
    private boolean readMark;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    private LocalDateTime senderDeleteAt;
    private LocalDateTime ReceiverDeleteAt;


    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private List<NoteImg> noteImgList = new ArrayList<>();

    public void addNoteImg(NoteImg noteImg){
        noteImg.setNoteImg(this);
        noteImgList.add(noteImg);
    }
}
