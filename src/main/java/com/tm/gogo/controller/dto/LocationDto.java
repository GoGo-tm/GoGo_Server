package com.tm.gogo.controller.dto;

import com.tm.gogo.domain.Location;
import lombok.Builder;
import lombok.Getter;

public class LocationDto {
    @Getter
    @Builder
    public static class Response {
        private String name;
        private Float latitude;
        private Float longitude;

        public static Response of(Location location) {
            return Response.builder()
                    .name(location.getName())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
        }
    }
}
