package com.tm.gogo.domain.hiking_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HikingLogImageRepository extends JpaRepository<HikingLogImage, Long> {
    @Modifying
    @Transactional
    @Query("delete from HikingLogImage h where h in :hikingLogImages")
    void deleteAllByHikingLogImages(@Param("hikingLogImages") List<HikingLogImage> hikingLogImages);
}
