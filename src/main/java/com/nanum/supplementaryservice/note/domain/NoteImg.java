package com.nanum.supplementaryservice.note.domain;

import com.nanum.config.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NoteImg  extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String savedName;

    @Column(nullable = false)
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Note note;

    public void setNoteImg(Note note){
        this.note = note;
    }
}
