package com.tm.gogo.domain.recommend_trail;

import com.tm.gogo.domain.favorite_trail.FavoriteTrailService;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.web.hiking_trail.HikingTrailDto;
import com.tm.gogo.web.recommend_trail.RecommendTrailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendTrailService {

    private final RecommendTrailRepository recommendTrailRepository;
    private final FavoriteTrailService favoriteTrailService;

    public RecommendTrailResponse findRecommendTrails(Long memberId, RecommendTrailTheme theme) {
        List<HikingTrail> hikingTrails = recommendTrailRepository.findByTheme(theme).stream()
                .map(RecommendTrail::getHikingTrail)
                .collect(Collectors.toList());

        Set<Long> favoriteTrailIds = findFavoriteTrailIdsIfMemberExist(memberId, hikingTrails);

        List<HikingTrailDto> results = hikingTrails.stream()
                .map(HikingTrailDto::of)
                .peek(dto -> dto.updateByFavoriteIdSet(favoriteTrailIds))
                .collect(Collectors.toList());

        return new RecommendTrailResponse(results);
    }

    private Set<Long> findFavoriteTrailIdsIfMemberExist(Long memberId, List<HikingTrail> hikingTrails) {
        if (memberId == null) {
            return Collections.emptySet();
        }

        return favoriteTrailService.findFavoriteTrailIds(memberId, hikingTrails);
    }
}
