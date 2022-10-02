package com.nanum.supplementaryservice.note.dto;

import com.nanum.supplementaryservice.note.domain.NoteImg;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class NoteImgDto {

    private String originName;
    private String savedName;
    private String imgPath;

    public NoteImg noteImgDtoToEntity(){
        return NoteImg.builder()
                .imgPath(getImgPath())
                .originName(getOriginName())
                .savedName(getSavedName())
                .build();
    }
}
