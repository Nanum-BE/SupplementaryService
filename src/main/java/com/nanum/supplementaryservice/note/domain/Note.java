package com.nanum.supplementaryservice.note.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nanum.config.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "note")
@SQLDelete(sql = "update note set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
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


    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<NoteImg> noteImgList = new ArrayList<>();

    public void addNoteImg(NoteImg noteImg){
        noteImg.setNoteImg(this);
        noteImgList.add(noteImg);
    }
}
