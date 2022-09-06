package com.tm.gogo.domain.favorite_trail;

import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteTrailRepository extends JpaRepository<FavoriteTrail, Long> {
    Optional<FavoriteTrail> findByMemberAndHikingTrail(Member member, HikingTrail hikingTrail);
}
