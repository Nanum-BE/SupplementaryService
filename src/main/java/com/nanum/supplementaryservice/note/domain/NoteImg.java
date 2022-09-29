package com.nanum.supplementaryservice.note.domain;

import com.nanum.config.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "update note_img set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
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

    private LocalDateTime deleteAt;
    public void setNoteImg(Note note){
        this.note = note;
    }
}
