package com.tm.gogo.web.hiking_trail;

import com.tm.gogo.domain.hiking_trail.HikingTrailService;
import com.tm.gogo.helper.SecurityUtil;
import com.tm.gogo.parameter.Scrollable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "/hiking-trails", description = "등산로 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hiking-trails")
public class HikingTrailController {

    private final HikingTrailService hikingTrailService;

    @Operation(summary = "등산로 리스트 조회", description = "파라미터를 통해 필터링, 정렬 가능")
    @GetMapping
    public ResponseEntity<HikingTrailsResponse> findHikingTrails(HikingTrailCondition condition, Scrollable scrollable) {
        return ResponseEntity.ok(
                hikingTrailService.findHikingTrails(condition, scrollable)
        );
    }

    @Operation(summary = "즐겨찾기에 등록된 등산로 리스트 조회", description = "로그인 상태여야 하며 파라미터를 통해 필터링, 정렬 가능")
    @GetMapping("/favorite")
    public ResponseEntity<HikingTrailsResponse> findFavoriteHikingTrails(HikingTrailCondition condition, Scrollable scrollable) {
        return ResponseEntity.ok(
                hikingTrailService.findFavoriteHikingTrails(SecurityUtil.getCurrentMemberId(), condition, scrollable)
        );
    }


    @Operation(summary = "등산로 하나 조회", description = "등산로 한개에 대해 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등산로 조회 성공"),
            @ApiResponse(responseCode = "404", description = "등산로 정보가 존재하지 않음", content = @Content)
    })
    @GetMapping("/{hikingTrailId}")
    public ResponseEntity<HikingTrailDetailResponse> findHikingTrail(@PathVariable Long hikingTrailId) {
        return ResponseEntity.ok(
                hikingTrailService.findHikingTrail(hikingTrailId)
        );
    }
}
