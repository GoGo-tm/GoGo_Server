package com.tm.gogo.web.recommend_trail;

import com.tm.gogo.web.hiking_trail.HikingTrailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecommendTrailResponse {
    private List<HikingTrailDto> recommendTrails;

    public RecommendTrailResponse(List<HikingTrailDto> recommendTrails) {
        this.recommendTrails = recommendTrails;
    }
}
