package com.nanum.supplementaryservice.police.application;

import com.nanum.supplementaryservice.police.dto.PoliceDto;
import com.nanum.supplementaryservice.police.vo.PoliceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PoliceService {
    /*
1. 신고 생성
2. 신고 조회
3. 신고 상세 조회
3. 신고 승인
4.  신고 삭제
 */
    boolean createPolice(PoliceDto policeDto);

    Page<PoliceResponse> retrievePolices(Pageable pageable);

    boolean acceptedPolice(Long policeId);
    void  deletePolice(Long policeId);
}
