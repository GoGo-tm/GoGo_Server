package com.tm.gogo.domain.hiking_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface HikingLogRepository extends JpaRepository<HikingLog, Long> {
    @Override
    @Modifying
    @Transactional
    @Query("delete from HikingLog h where h.id = :id")
    void deleteById(@Param("id") Long hikingLogId);
}
