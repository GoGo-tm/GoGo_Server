package com.tm.gogo.domain.hiking_trail;

import com.tm.gogo.domain.favorite_trail.FavoriteTrailService;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_trail.HikingTrailCondition;
import com.tm.gogo.web.hiking_trail.HikingTrailDetailResponse;
import com.tm.gogo.web.hiking_trail.HikingTrailDto;
import com.tm.gogo.web.hiking_trail.HikingTrailsResponse;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.tm.gogo.web.response.ErrorCode.HIKING_TRAIL_NOT_FOUND;

@Service
@CacheConfig(cacheNames = "hiking_trail")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HikingTrailService {

    private final HikingTrailQueryRepository hikingTrailQueryRepository;
    private final HikingTrailRepository hikingTrailRepository;
    private final FavoriteTrailService favoriteTrailService;

    @Cacheable(key = "#condition + '::' + #scrollable", unless = "#result == null")
    public HikingTrailsResponse findHikingTrails(HikingTrailCondition condition, Scrollable scrollable) {
        return hikingTrailQueryRepository.findHikingTrails(condition, scrollable);
    }

    @Cacheable(key = "#memberId + '::' + #condition + '::' + #scrollable", unless = "#result == null")
    public HikingTrailsResponse findHikingTrailsOfMember(Long memberId, HikingTrailCondition condition, Scrollable scrollable) {
        HikingTrailsResponse hikingTrails = hikingTrailQueryRepository.findHikingTrails(condition, scrollable);
        updateFavorite(memberId, hikingTrails);
        return hikingTrails;
    }

    private void updateFavorite(Long memberId, HikingTrailsResponse hikingTrails) {
        List<Long> hikingTrailIds = hikingTrails.getContents().stream()
                .map(HikingTrailDto::getId)
                .collect(Collectors.toList());

        List<HikingTrail> trails = hikingTrailRepository.findAllById(hikingTrailIds);

        Set<Long> favoriteTrailIds = favoriteTrailService.findFavoriteTrailIds(memberId, trails);

        hikingTrails.updateContentFavorites(favoriteTrailIds);
    }

    @Cacheable(key = "'favorite::' + #memberId + '::' + #condition + '::' + #scrollable", unless = "#result == null")
    public HikingTrailsResponse findFavoriteHikingTrails(Long memberId, HikingTrailCondition condition, Scrollable scrollable) {
        return hikingTrailQueryRepository.findFavoriteHikingTrails(memberId, condition, scrollable);
    }

    @Cacheable(key = "#hikingTrailId", unless = "#result == null")
    public HikingTrailDetailResponse findHikingTrail(Long hikingTrailId) {
        return hikingTrailRepository.findById(hikingTrailId)
                .map(HikingTrailDetailResponse::of)
                .orElseThrow(() -> new ApiException(HIKING_TRAIL_NOT_FOUND, "등산로 정보가 없습니다. hikingTrailId: " + hikingTrailId));
    }
}
