package com.tm.gogo.web.hiking_trail;

import com.tm.gogo.domain.hiking_trail.Geometry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeometryDto {

    @Schema(example = "37.22589972640079")
    private String latitude;

    @Schema(example = "128.44706436148968")
    private String longitude;

    public static GeometryDto of(Geometry geometry) {
        return GeometryDto.builder()
                .latitude(geometry.getLatitude())
                .longitude(geometry.getLongitude())
                .build();
    }
}
