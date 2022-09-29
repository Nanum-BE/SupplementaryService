package com.nanum.supplementaryservice.note.infrastructure;

import com.nanum.supplementaryservice.note.domain.NoteImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteImgRepository extends JpaRepository<NoteImg,Long> {

//    void deleteAllByNoteIdInBatch(Long noteId);



    void deleteAllByNoteId(Long noteId);
}
