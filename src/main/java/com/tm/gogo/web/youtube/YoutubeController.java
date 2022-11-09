package com.tm.gogo.web.youtube;

import com.tm.gogo.domain.youtube.YoutubeService;
import com.tm.gogo.parameter.Scrollable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "/youtubes", description = "추천 유튜브 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/youtubes")
public class YoutubeController {

    private final YoutubeService youtubeService;

    @Operation(summary = "추천 유튜브 영상 조회")
    @GetMapping
    public ResponseEntity<YoutubesResponse> findYoutubes(Scrollable scrollable) {
        return ResponseEntity.ok(youtubeService.findYoutubes(scrollable));
    }
}
