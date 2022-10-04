package com.nanum.supplementaryservice.police.domain;

import com.nanum.config.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "update police set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
public class Police extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reportedUserId;

    @Column(nullable = false)
    private Long reporterId;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private Long typeId;

    @Column(length = 32, columnDefinition = "varchar(32) default 'WAITING'")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? Status.WAITING : this.status;
    }
}
