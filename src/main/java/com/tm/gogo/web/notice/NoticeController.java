package com.tm.gogo.web.notice;

import com.tm.gogo.domain.notice.NoticeService;
import com.tm.gogo.parameter.Scrollable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "/notices", description = "공지사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 조회", description = "무한 스크롤 방식으로 조회 가능")
    @GetMapping
    public ResponseEntity<NoticesResponse> findNotices(Scrollable scrollable) {
        return ResponseEntity.ok(noticeService.findNotices(scrollable));
    }
}
