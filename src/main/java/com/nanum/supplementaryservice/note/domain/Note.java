package com.nanum.supplementaryservice.note.domain;

import com.nanum.config.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notenote")
@Entity
public class Note {

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
    private Long sender;

    @Column(nullable = false)
    private Long receiver;

    private LocalDateTime senderDeleteAt;
    private LocalDateTime ReceiverDeleteAt;

}
