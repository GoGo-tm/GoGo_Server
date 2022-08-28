package com.tm.gogo.web.hiking_trail;

import com.tm.gogo.domain.hiking_trail.Difficulty;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class HikingTrailDetailResponse {

    @Schema(example = "관악산 A코스")
    private String name;

    @Schema(example = "서울시 강남구 대치동")
    private String address;

    @Schema(example = "EASY")
    private Difficulty difficulty;

    @Schema(example = "123", description = "단위: m")
    private Integer length;

    @Schema(example = "1", description = "단위: 분")
    private Integer uptime;

    @Schema(example = "2", description = "단위: 분")
    private Integer downtime;

    @Schema(example = "https://www.forest.go.kr/images/data/down/mountain/mountain_image.jpg")
    private String imageUrl;

    private List<GeometryDto> geometries;

    public static HikingTrailDetailResponse of(HikingTrail hikingTrail) {
        return HikingTrailDetailResponse.builder()
                .name(hikingTrail.getName())
                .address(hikingTrail.getAddress())
                .difficulty(hikingTrail.getDifficulty())
                .length(hikingTrail.getLength())
                .uptime(hikingTrail.getUptime())
                .downtime(hikingTrail.getDowntime())
                .imageUrl(hikingTrail.getImageUrl())
                .geometries(hikingTrail.getGeometries().stream().map(GeometryDto::of).collect(Collectors.toList()))
                .build();
    }
}
