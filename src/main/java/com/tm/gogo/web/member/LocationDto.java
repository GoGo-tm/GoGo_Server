package com.tm.gogo.web.member;

import com.tm.gogo.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {

    private String name;
    private Float latitude;
    private Float longitude;

    public static LocationDto of(Location location) {
        return LocationDto.builder()
                .name(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    public Location toLocation() {
        return Location.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
