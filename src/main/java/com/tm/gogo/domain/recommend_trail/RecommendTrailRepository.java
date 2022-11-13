package com.tm.gogo.domain.recommend_trail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendTrailRepository extends JpaRepository<RecommendTrail, Long> {
    List<RecommendTrail> findByTheme(RecommendTrailTheme theme);
}
