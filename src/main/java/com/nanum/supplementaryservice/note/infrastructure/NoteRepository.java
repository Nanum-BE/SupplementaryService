package com.nanum.supplementaryservice.note.infrastructure;

import com.nanum.supplementaryservice.note.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository  extends JpaRepository<Note, Long> {
        Page<Note> findBySenderIdAndDeleterIdIsNot(Long senderId,Long deleterId, Pageable pageable);

    Page<Note> findByReceiverIdAndDeleterIdIsNot(Long receiverId, Long deleterId,Pageable pageable);

    List<Note> findByReceiverId(Long userId);
    List<Note> findBySenderId(Long userId);

    @Override
    @EntityGraph(attributePaths = {"noteImgList"})
    Optional<Note> findById(Long aLong);



}

