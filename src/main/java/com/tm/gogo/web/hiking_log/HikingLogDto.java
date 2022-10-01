package com.tm.gogo.web.hiking_log;

import com.querydsl.core.annotations.QueryProjection;
import com.tm.gogo.domain.hiking_trail.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class HikingLogDto {
    private Long id;
    private String hikingTrailName;
    private String hikingTrailImageUrl;
    private LocalDateTime hikingDate;
    private Integer starRating;
    private Difficulty difficulty;
    private String address;
    private Integer length;
    private List<String> imageUrls;

    @QueryProjection
    public HikingLogDto(Long id, String hikingTrailName,String hikingTrailImageUrl, LocalDateTime hikingDate, Integer starRating, Difficulty difficulty, String address, Integer length) {
        this.id = id;
        this.hikingTrailName = hikingTrailName;
        this.hikingTrailImageUrl = hikingTrailImageUrl;
        this.hikingDate = hikingDate;
        this.starRating = starRating;
        this.difficulty = difficulty;
        this.address = address;
        this.length = length;
        this.imageUrls = new ArrayList<>();
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
