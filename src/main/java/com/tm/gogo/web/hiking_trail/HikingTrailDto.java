package com.tm.gogo.web.hiking_trail;

import com.querydsl.core.annotations.QueryProjection;
import com.tm.gogo.domain.hiking_trail.Difficulty;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
public class HikingTrailDto {

    @Schema(example = "2505")
    private Long id;

    @Schema(example = "https://www.forest.go.kr/images/data/down/mountain/20000059_3.jpg")
    private String imageUrl;

    @Schema(example = "관악산")
    private String name;

    @Schema(example = "서울특별시 관악구 신림동")
    private String address;

    @Schema(example = "0")
    private Long favoriteCount;

    @Schema(example = "EASY", description = "EASY|NORMAL|HARD")
    private Difficulty difficulty;

    @Schema(example = "43690", description = "단위: m")
    private Integer length;

    @Schema(example = "false", description = "회원인 경우 즐겨찾기 여부")
    private Boolean favorite;

    @Builder
    @QueryProjection
    public HikingTrailDto(Long id, String imageUrl, String name, String address, Long favoriteCount, Difficulty difficulty, Integer length) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.address = address;
        this.favoriteCount = favoriteCount;
        this.difficulty = difficulty;
        this.length = length;
        this.favorite = false;
    }

    public static HikingTrailDto of(HikingTrail hikingTrail) {
        return HikingTrailDto.builder()
                .id(hikingTrail.getId())
                .imageUrl(hikingTrail.getImageUrl())
                .name(hikingTrail.getName())
                .address(hikingTrail.getAddress())
                .favoriteCount(hikingTrail.getFavoriteCount())
                .difficulty(hikingTrail.getDifficulty())
                .length(hikingTrail.getLength())
                .build();
    }

    public void updateByFavoriteIdSet(Set<Long> favoriteTrailIds) {
        this.favorite = favoriteTrailIds.contains(this.id);
    }

    public void updateFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
