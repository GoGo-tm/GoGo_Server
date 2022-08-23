package com.tm.gogo;

import com.tm.gogo.domain.hiking_trail.HikingTrail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HikingTrailRepository extends JpaRepository<HikingTrail, Long> {
}
