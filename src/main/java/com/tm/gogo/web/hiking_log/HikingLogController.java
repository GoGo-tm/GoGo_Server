package com.tm.gogo.web.hiking_log;

import com.tm.gogo.domain.hiking_log.HikingLogService;
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
            @ApiResponse(responseCode = "200", description = "등산 기록 리스트 조회"),
    })
    @GetMapping()
    public ResponseEntity<HikingLogResponse> findHikingLogs(Scrollable scrollable) {
        return ResponseEntity.ok(hikingLogService.findHikingLogs(SecurityUtil.getCurrentMemberId(), scrollable));
    }

    @Operation(summary = "등산 기록 하나 조회", description = "등산 기록 한개에 대해 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등산로그 조회 성공"),
            @ApiResponse(responseCode = "404", description = "등산로그 정보가 존재하지 않음", content = @Content)
    })
    @GetMapping("/{hikingLogId}")
    public ResponseEntity<HikingLogDetailResponse> findHikingLog(@PathVariable Long hikingLogId) {
        return ResponseEntity.ok(hikingLogService.findHikingLog(hikingLogId));
    }

    @Operation(summary = "등산 기록 하나 삭제", description = "등산 기록 한개 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등산로그 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "등산로그 멤버와 주어진 멤버가 불일치"),
            @ApiResponse(responseCode = "404", description = "등산로그 정보가 존재하지 않음", content = @Content)
    })
    @DeleteMapping("/{hikingLogId}")
    public ResponseEntity<Void> deleteHikingLog(@PathVariable Long hikingLogId) {
        hikingLogService.deleteHikingLog(SecurityUtil.getCurrentMemberId(), hikingLogId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "등산 기록 수정", description = "등산 기록 한개 수정하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등산로그 수정 성공"),
            @ApiResponse(responseCode = "400", description = "등산로그 멤버와 주어진 멤버가 불일치"),
            @ApiResponse(responseCode = "404", description = "등산로그 정보가 존재하지 않음", content = @Content)
    })
    @PutMapping("/{hikingLogId}")
    public ResponseEntity<Long> updateHikingLog(@PathVariable Long hikingLogId, @RequestBody HikingLogRequest hikingLogRequest) {
        return ResponseEntity.ok(hikingLogService.updateHikingLog(SecurityUtil.getCurrentMemberId(), hikingLogId, hikingLogRequest));
    }
}
