package com.tm.gogo.web.hiking_log;

import com.tm.gogo.helper.SecurityUtil;
import com.tm.gogo.domain.hiking_log.HikingLogService;
import com.tm.gogo.parameter.Scrollable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "/hiking-log", description = "등산 기록")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hiking-log")
public class HikingLogController {
    private final HikingLogService hikingLogService;

    @Operation(summary = "등산 기록 생성하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등산 기록 생성 성공"),
    })
    @PostMapping()
    public ResponseEntity<Long> createHikingLog(@RequestBody HikingLogRequest hikingLogRequest) {
        return ResponseEntity.ok(hikingLogService.createHikingLog(SecurityUtil.getCurrentMemberId(), hikingLogRequest));
    }

    @Operation(summary = "등산 기록 조회하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등산 기록 조회 성공"),
    })
    @GetMapping()
    public ResponseEntity<HikingLogResponse> findHikingLogs(Scrollable scrollable) {
        return ResponseEntity.ok(hikingLogService.findHikingLogs(SecurityUtil.getCurrentMemberId(), scrollable));
    }

}
