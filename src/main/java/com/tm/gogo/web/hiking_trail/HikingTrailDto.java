package com.tm.gogo.web.hiking_trail;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HikingTrailDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "https://www.forest.go.kr/images/data/down/mountain/mountain_image.jpg")
    private String imageUrl;

    @Schema(example = "관악산 A코스")
    private String name;

    @QueryProjection
    public HikingTrailDto(Long id, String imageUrl, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
    }
}
