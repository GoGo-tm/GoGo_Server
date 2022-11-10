package com.tm.gogo.web.favorite_trail;

import com.tm.gogo.domain.favorite_trail.FavoriteTrailService;
import com.tm.gogo.helper.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "등산로 즐겨찾기(좋아요)", description = "사용자가 특정 등산로를 즐겨찾기 리스트에 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨찾기 등록 성공"),
            @ApiResponse(responseCode = "404", description = "사용자/등산로 정보가 존재하지 않음", content = @Content),
            @ApiResponse(responseCode = "409", description = "이미 즐겨찾기 된 등산로", content = @Content),
    })
    @PostMapping("/{hikingTrailId}/favorite")
    public ResponseEntity<Void> registerFavorite(@PathVariable Long hikingTrailId) {
        favoriteTrailService.registerFavorite(SecurityUtil.getCurrentMemberId(), hikingTrailId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "등산로 즐겨찾기(좋아요)", description = "사용자가 특정 등산로를 즐겨찾기 리스트에 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨찾기 등록 성공"),
            @ApiResponse(responseCode = "404", description = "사용자/등산로/즐겨찾기 정보가 존재하지 않음", content = @Content)
    })
    @DeleteMapping("/{hikingTrailId}/favorite")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long hikingTrailId) {
        favoriteTrailService.deleteFavorite(SecurityUtil.getCurrentMemberId(), hikingTrailId);
        return ResponseEntity.ok().build();
    }
}
