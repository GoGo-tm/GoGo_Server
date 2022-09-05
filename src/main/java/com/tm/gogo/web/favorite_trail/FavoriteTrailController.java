package com.tm.gogo.web.favorite_trail;

import com.tm.gogo.domain.favorite_trail.FavoriteTrailService;
import com.tm.gogo.helper.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "/hiking-trails", description = "등산로 즐겨찾기 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hiking-trails")
public class FavoriteTrailController {

    private final FavoriteTrailService favoriteTrailService;

    @PostMapping("/{hikingTrailId}/favorite")
    public ResponseEntity<Void> registerFavorite(@PathVariable Long hikingTrailId) {
        favoriteTrailService.registerFavorite(SecurityUtil.getCurrentMemberId(), hikingTrailId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{hikingTrailId}/favorite")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long hikingTrailId) {
        favoriteTrailService.deleteFavorite(SecurityUtil.getCurrentMemberId(), hikingTrailId);
        return ResponseEntity.ok().build();
    }
}
