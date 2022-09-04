package com.tm.gogo.domain.hiking_trail;

import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_trail.HikingTrailCondition;
import com.tm.gogo.web.hiking_trail.HikingTrailDetailResponse;
import com.tm.gogo.web.hiking_trail.HikingTrailsResponse;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tm.gogo.web.response.ErrorCode.HIKING_LOG_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HikingTrailService {

    private final HikingTrailQueryRepository hikingTrailQueryRepository;
    private final HikingTrailRepository hikingTrailRepository;

    public HikingTrailsResponse findHikingTrails(HikingTrailCondition condition, Scrollable scrollable) {
        return hikingTrailQueryRepository.findHikingTrails(condition, scrollable);
    }

    public HikingTrailsResponse findFavoriteHikingTrails(Long memberId, HikingTrailCondition condition, Scrollable scrollable) {
        return hikingTrailQueryRepository.findFavoriteHikingTrails(memberId, condition, scrollable);
    }

    public HikingTrailDetailResponse findHikingTrail(Long hikingTrailId) {
        return hikingTrailRepository.findById(hikingTrailId)
                .map(HikingTrailDetailResponse::of)
                .orElseThrow(() -> new ApiException(HIKING_LOG_NOT_FOUND, "등산로 정보가 없습니다. hikingTrailId: " + hikingTrailId));
    }
}
