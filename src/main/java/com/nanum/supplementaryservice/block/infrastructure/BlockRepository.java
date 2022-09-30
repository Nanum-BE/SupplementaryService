package com.nanum.supplementaryservice.block.infrastructure;

import com.nanum.supplementaryservice.block.domain.Block;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Page<Block> findByBlockerId(Long blockerId, Pageable pageable);

    boolean findByBlockerIdAndBlockedUserId(Long blockerId, Long blockedUserId);
}
