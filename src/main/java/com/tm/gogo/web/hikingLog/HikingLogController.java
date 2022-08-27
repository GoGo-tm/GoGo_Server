package com.tm.gogo.web.hikingLog;

import com.tm.gogo.helper.SecurityUtil;
import com.tm.gogo.hikingLog.HikingLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hikingLog")
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
}
