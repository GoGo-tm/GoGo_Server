package com.tm.gogo.web.recommend_trail;

import com.tm.gogo.domain.recommend_trail.RecommendTrailService;
import com.tm.gogo.domain.recommend_trail.RecommendTrailTheme;
import com.tm.gogo.helper.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "/recommend-trails", description = "추천 등산로 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend-trails")
public class RecommendTrailController {

    private final RecommendTrailService recommendTrailService;

    @Operation(summary = "추천 등산로 조회", description = "무한 스크롤 방식으로 조회 가능")
    @GetMapping("/{theme}")
    public ResponseEntity<RecommendTrailResponse> findRecommendTrails(@PathVariable RecommendTrailTheme theme) {
        return ResponseEntity.ok(
                recommendTrailService.findRecommendTrails(SecurityUtil.getCurrentMemberId(), theme)
        );
    }
}
