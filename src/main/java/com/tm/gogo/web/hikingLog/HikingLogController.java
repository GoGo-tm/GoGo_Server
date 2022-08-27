package com.tm.gogo.web.hikingLog;

import com.tm.gogo.hikingLog.HikingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hikingLog")
public class HikingLogController {
    private final HikingLogService hikingLogService;

    public ResponseEntity<Long> createHikingLog(@RequestBody HikingLogRequest hikingLogRequest) {
        return ResponseEntity.ok(hikingLogService.createHikingLog(hikingLogRequest));
    }
}
