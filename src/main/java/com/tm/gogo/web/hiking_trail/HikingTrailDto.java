package com.tm.gogo.web.hiking_trail;

import com.querydsl.core.annotations.QueryProjection;
import com.tm.gogo.domain.hiking_trail.Difficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @QueryProjection
    public HikingTrailDto(Long id, String imageUrl, String name, String address, Long favoriteCount, Difficulty difficulty, Integer length) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.address = address;
        this.favoriteCount = favoriteCount;
        this.difficulty = difficulty;
        this.length = length;
    }
}
