package com.nanum.supplementaryservice.police.infrastructure;

import com.nanum.supplementaryservice.police.domain.Police;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliceRepository extends JpaRepository<Police, Long> {

    boolean existsByReporterIdAndReportedUserId(Long reporterId, Long reportedUserId);
}
